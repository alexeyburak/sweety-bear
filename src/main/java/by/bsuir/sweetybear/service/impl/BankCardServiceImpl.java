package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.dto.BankCardDTO;
import by.bsuir.sweetybear.model.BankCard;
import by.bsuir.sweetybear.model.Order;
import by.bsuir.sweetybear.repository.BankCardRepository;
import by.bsuir.sweetybear.service.BankCardService;
import by.bsuir.sweetybear.validator.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Service
@Slf4j
@AllArgsConstructor
public class BankCardServiceImpl implements BankCardService {

    private final BankCardRepository bankCardRepository;

    public boolean makeOrderPayment(Order order, BankCardDTO bankCardDTO) {
        BankCard bankCard = toEntity(bankCardDTO);
        bankCard.setUser(order.getUser());

        if (!isBankCardValid(bankCard))
            return false;

        if (!isEnoughMoney(bankCard.getBalance(), order.getSum()))
            return false;

        BigDecimal newBalanceOnBanCard = calculateNewBalance(bankCard.getBalance(), order.getSum());
        bankCard.setBalance(newBalanceOnBanCard);

        bankCardRepository.save(bankCard);

        return true;
    }

    private boolean isEnoughMoney(BigDecimal balance, BigDecimal price) {
        return balance.compareTo(price) > 0;
    }

    private BigDecimal calculateNewBalance(BigDecimal balance, BigDecimal price) {
        return balance.subtract(price);
    }

    public boolean isBankCardValid(BankCard bankCard) {
        return ValidatorFactory.getInstance().getCardNameValidator().isValid(bankCard.getCardholderName()) &&
                ValidatorFactory.getInstance().getCardNumberValidator().isValid(bankCard.getCardNumber().toString()) &&
                ValidatorFactory.getInstance().getCvvValidator().isValid(bankCard.getCvv().toString()) &&
                ValidatorFactory.getInstance().getMonthValidator().isValid(bankCard.getExpirationMonth().toString()) &&
                ValidatorFactory.getInstance().getYearValidator().isValid(bankCard.getExpirationYear().toString());
    }

    //TODO card month and year building
    private BankCard toEntity(BankCardDTO bankCardDTO) {
        return BankCard.builder()
                .cardNumber(bankCardDTO.getCardNumber())
                .cardholderName(bankCardDTO.getCardholderName())
                .cvv(bankCardDTO.getCvv())
                .build();
    }
}
