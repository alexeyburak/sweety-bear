package by.bsuir.sweetybear.service;

import java.math.BigDecimal;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public interface PaymentService {

    boolean isEnoughMoney(BigDecimal balance, BigDecimal price);
    BigDecimal calculateNewBalance(BigDecimal balance, BigDecimal price);
}
