package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.dto.AccessibleBankCardDTO;
import by.bsuir.sweetybear.dto.BankCardDTO;
import by.bsuir.sweetybear.exception.ApiRequestException;
import by.bsuir.sweetybear.model.BankCard;
import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.repository.BankCardRepository;
import by.bsuir.sweetybear.service.BankCardService;
import by.bsuir.sweetybear.service.PaymentService;
import by.bsuir.sweetybear.validator.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Service
@Slf4j
@AllArgsConstructor
public class BankCardServiceImpl implements BankCardService, PaymentService {

    private final BankCardRepository bankCardRepository;

    @Override
    public List<AccessibleBankCardDTO> getBankCardDTOListByUserId(Long userId) {
        return bankCardRepository.getBankCardsByUserId(userId)
                .stream()
                .map(bankCard -> AccessibleBankCardDTO.builder()
                        .id(bankCard.getId())
                        .cardNumber("***" + String.valueOf(bankCard.getCardNumber()).substring(12))
                        .expiryDate(bankCard.getExpirationMonth() + "/" + bankCard.getExpirationYear())
                        .build()
                )
                .toList();
    }

    @Override
    public boolean addNewBankCard(BankCardDTO bankCardDTO, User user) {
        BankCard bankCard = toEntity(bankCardDTO);

        if (!isBankCardReadyToAdd(bankCard)) {
            log.warn("Bank card validation error.");
            return false;
        }
        if (isBankCardExist(bankCard)) {
            log.warn("Bank card is already exists.");
            return false;
        }

        user.addBankCardToUser(bankCard);

        bankCardRepository.save(bankCard);
        log.info("Add bank card to user. User id: {}", user.getId());
        return true;
    }

    private boolean isBankCardReadyToAdd(BankCard bankCard) {
        return isBankCardValid(bankCard) &&
                isCardDateValid(bankCard.getExpirationMonth(), bankCard.getExpirationYear());
    }

    @Override
    public boolean isBankCardValid(BankCard bankCard) {
        return ValidatorFactory.getInstance().getCardNameValidator().isValid(bankCard.getCardholderName()) &&
                ValidatorFactory.getInstance().getCardNumberValidator().isValid(bankCard.getCardNumber().toString()) &&
                ValidatorFactory.getInstance().getCvvValidator().isValid(bankCard.getCvv().toString()) &&
                ValidatorFactory.getInstance().getMonthValidator().isValid(bankCard.getExpirationMonth().toString()) &&
                ValidatorFactory.getInstance().getYearValidator().isValid(bankCard.getExpirationYear().toString());
    }

    @Override
    public boolean isBankCardExist(BankCard bankCard) {
        BankCard bankCardDb = bankCardRepository.findByCardNumber(bankCard.getCardNumber());
        if (bankCardDb != null) {
            return isBankCardInformationCorrect(bankCard, bankCardDb.getCardNumber(), bankCardDb.getExpirationMonth(), bankCardDb.getExpirationYear(), bankCardDb.getCvv());
        }
        return false;
    }

    @Override
    public boolean isBankCardInformationCorrect(BankCard bankCard, long cardNumber, int expirationMonth, int expirationYear, int cvv) {
        if (bankCard.getCardNumber() != cardNumber) return false;
        if (bankCard.getExpirationMonth() != expirationMonth) return false;
        if (bankCard.getExpirationYear() != expirationYear) return false;
        return bankCard.getCvv() == cvv;
    }

    @Override
    public boolean debitingMoneyFromTheBankCard(Order order, Long paymentId) {
        BankCard bankCard = this.getBankCardById(paymentId);

        if (!isBankCardReadyToDebiting(order, bankCard)) {
            log.warn("Bank card validation error.");
            return false;
        }

        BigDecimal newBalanceOnBankCard = calculateNewBalance(bankCard.getBalance(), order.getSum());
        bankCard.setBalance(newBalanceOnBankCard);

        bankCardRepository.save(bankCard);
        log.info("Debiting money from the bank card. Card id: {}", paymentId);
        return true;
    }

    private boolean isBankCardReadyToDebiting(Order order, BankCard bankCard) {
        return isEnoughMoney(bankCard.getBalance(), order.getSum()) &&
                isCardDateValid(bankCard.getExpirationMonth(), bankCard.getExpirationYear());
    }

    @Override
    public boolean isEnoughMoney(BigDecimal balance, BigDecimal price) {
        return balance.compareTo(price) > 0;
    }

    @Override
    public boolean isCardDateValid(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYearWithoutMillennium = Integer.parseInt(String.valueOf(calendar.get(Calendar.YEAR)).substring(2));

        return month >= currentMonth && year >= currentYearWithoutMillennium;
    }

    @Override
    public BigDecimal calculateNewBalance(BigDecimal balance, BigDecimal price) {
        return balance.subtract(price);
    }

    @Override
    public BankCard getBankCardById(Long id) {
        return bankCardRepository.findById(id)
                .orElseThrow(() -> new ApiRequestException("Bank card not found"));
    }

    @Override
    @Transactional
    public void deleteBankCardById(Long id) {
        bankCardRepository.deleteByBankCardId(id);
        log.info("Bank card with id {} was deleted.", id);
    }

    private BankCard toEntity(BankCardDTO bankCardDTO) {
        String expiryDate = bankCardDTO.getExpiryDate();
        return BankCard.builder()
                .balance(BigDecimal.valueOf(1_000_000L))
                .cardNumber(bankCardDTO.getCardNumber())
                .cardholderName(bankCardDTO.getCardholderName().toUpperCase())
                .cvv(bankCardDTO.getCvv())
                .expirationMonth(Integer.parseInt(expiryDate.substring(0, expiryDate.indexOf("/"))))
                .expirationYear(Integer.parseInt(expiryDate.substring(expiryDate.indexOf("/") + 1)))
                .build();
    }
}
