package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.dto.BankCardDTO;
import by.bsuir.sweetybear.model.BankCard;
import by.bsuir.sweetybear.model.Order;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public interface BankCardService {

    boolean makeOrderPayment(Order order, BankCardDTO bankCardDTO);
    boolean isBankCardExist(BankCard bankCard);
    boolean isBankCardInformationCorrect(BankCard bankCard, long cardNumber, int expirationMonth, int expirationYear, int cvv);
    boolean isBankCardValid(BankCard bankCard);
}
