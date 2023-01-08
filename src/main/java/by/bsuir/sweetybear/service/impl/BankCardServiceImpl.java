package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.dto.BankCardDTO;
import by.bsuir.sweetybear.model.BankCard;
import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.repository.BankCardRepository;
import by.bsuir.sweetybear.service.BankCardService;
import by.bsuir.sweetybear.service.PaymentService;
import by.bsuir.sweetybear.validator.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    private static final String CURRENT_MILLENNIUM = "20";
    private final BankCardRepository bankCardRepository;

    @Override
    public List<BankCard> getBankCardsByUserId(Long userId) {
        return bankCardRepository.getBankCardsByUserId(userId);
    }

    @Override
    public boolean debitingMoneyFromTheBankCard(Order order, BankCardDTO bankCardDTO) {
        BankCard bankCard = toEntity(bankCardDTO);
        bankCard.setUser(order.getUser());

        if (bankCardDontReadyToDebited(order, bankCard)) {
            log.warn("Bank card validation error.");
            return false;
        }

        BigDecimal newBalanceOnBankCard = calculateNewBalance(bankCard.getBalance(), order.getSum());
        bankCard.setBalance(newBalanceOnBankCard);

        bankCardRepository.save(bankCard);
        log.info("Debiting money from the bank card. Card number: {}", bankCard.getCardNumber());
        return true;
    }

    private boolean bankCardDontReadyToDebited(Order order, BankCard bankCard) {
        return !isBankCardValid(bankCard) ||
                !isEnoughMoney(bankCard.getBalance(), order.getSum()) ||
                !isCardDateValid(bankCard.getExpirationMonth(), bankCard.getExpirationYear());
    }

    private boolean isCardDateValid(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        if (Integer.parseInt(CURRENT_MILLENNIUM + year) < currentYear) return true;
        if (year == currentYear) {
            return month < currentMonth;
        }
        return false;
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
    public boolean isEnoughMoney(BigDecimal balance, BigDecimal price) {
        return balance.compareTo(price) > 0;
    }

    @Override
    public BigDecimal calculateNewBalance(BigDecimal balance, BigDecimal price) {
        return balance.subtract(price);
    }

    @Override
    public boolean isBankCardValid(BankCard bankCard) {
        return ValidatorFactory.getInstance().getCardNameValidator().isValid(bankCard.getCardholderName()) &&
                ValidatorFactory.getInstance().getCardNumberValidator().isValid(bankCard.getCardNumber().toString()) &&
                ValidatorFactory.getInstance().getCvvValidator().isValid(bankCard.getCvv().toString()) &&
                ValidatorFactory.getInstance().getMonthValidator().isValid(bankCard.getExpirationMonth().toString()) &&
                ValidatorFactory.getInstance().getYearValidator().isValid(bankCard.getExpirationYear().toString());
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
