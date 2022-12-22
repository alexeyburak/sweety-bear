package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.dto.BankCardDTO;
import by.bsuir.sweetybear.model.Order;

import java.math.BigDecimal;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public interface PaymentService {

    boolean isEnoughMoney(BigDecimal balance, BigDecimal price);
    BigDecimal calculateNewBalance(BigDecimal balance, BigDecimal price);
    boolean debitingMoneyFromTheBankCard(Order order, BankCardDTO bankCardDTO);
}
