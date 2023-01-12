package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Order;

import java.math.BigDecimal;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public interface PaymentService {

    boolean isEnoughMoney(BigDecimal balance, BigDecimal price);
    boolean isCardDateValid(int month, int year);
    BigDecimal calculateNewBalance(BigDecimal balance, BigDecimal price);
    boolean debitingMoneyFromTheBankCard(Order order, Long paymentId);
}
