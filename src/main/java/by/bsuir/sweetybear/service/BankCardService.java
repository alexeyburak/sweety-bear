package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.dto.AccessibleBankCardDTO;
import by.bsuir.sweetybear.dto.BankCardDTO;
import by.bsuir.sweetybear.model.BankCard;
import by.bsuir.sweetybear.model.User;

import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public interface BankCardService {
    BankCard getBankCardById(Long id);
    List<AccessibleBankCardDTO> getBankCardDTOListByUserId(Long userId);
    boolean addNewBankCard(BankCardDTO bankCardDTO, User user);
    boolean isBankCardExist(BankCard bankCard);
    boolean isBankCardInformationCorrect(BankCard bankCard, long cardNumber, int expirationMonth, int expirationYear, int cvv);
    boolean isBankCardValid(BankCard bankCard);
    void deleteBankCardById(Long id);
}
