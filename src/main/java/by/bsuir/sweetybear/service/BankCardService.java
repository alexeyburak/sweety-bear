package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.BankCard;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public interface BankCardService {

    boolean isBankCardExist(BankCard bankCard);
    boolean isBankCardInformationCorrect(BankCard bankCard, long cardNumber, int expirationMonth, int expirationYear, int cvv);
    boolean isBankCardValid(BankCard bankCard);
}
