package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.dto.AccessibleBankCardDTO;
import by.bsuir.sweetybear.model.BankCard;

import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public interface BankCardService {

    List<AccessibleBankCardDTO> getBankCardsDTOByUserId(Long userId);
    boolean isBankCardExist(BankCard bankCard);
    boolean isBankCardInformationCorrect(BankCard bankCard, long cardNumber, int expirationMonth, int expirationYear, int cvv);
    boolean isBankCardValid(BankCard bankCard);
}
