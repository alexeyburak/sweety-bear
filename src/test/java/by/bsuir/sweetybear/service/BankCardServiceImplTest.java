package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.dto.bankcard.AccessibleBankCardDTO;
import by.bsuir.sweetybear.dto.bankcard.BankCardDTO;
import by.bsuir.sweetybear.model.BankCard;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.repository.BankCardRepository;
import by.bsuir.sweetybear.service.impl.BankCardServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Feb 2023
 */

@ExtendWith(MockitoExtension.class)
public class BankCardServiceImplTest {
    @Mock
    private BankCardRepository bankCardRepository;
    @InjectMocks
    private BankCardServiceImpl bankCardService;

    @Test
    void getBankCardDTOListByUserId_UserId_ReturnsListOfAccessibleBankCardDTO() {
        //given
        final long userId = 1L;
        List<BankCard> bankCards = List.of(
                BankCard.builder()
                        .cardNumber(1234567890123456L)
                        .expirationMonth(12)
                        .expirationYear(23)
                        .build()
        );

        //when
        Mockito.when(bankCardRepository.getBankCardsByUserId(userId)).thenReturn(bankCards);
        List<AccessibleBankCardDTO> result = bankCardService.getBankCardDTOListByUserId(userId);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("***3456", result.get(0).getCardNumber());
        Assertions.assertEquals("12/23", result.get(0).getExpiryDate());

    }

    @Test
    void addNewBankCard_ValidBankCard_True() {
        //given
        BankCardDTO bankCardDTO = getValidBankCardDTO();
        User user = User.builder()
                .bankCards(new ArrayList<>(
                        List.of(
                                BankCard.builder().build()
                        )
                ))
                .build();

        //when
        Mockito.when(bankCardRepository.findByCardNumber(bankCardDTO.getCardNumber())).thenReturn(null);
        boolean result = bankCardService.addNewBankCard(bankCardDTO, user);

        //then
        Assertions.assertTrue(result);
        Assertions.assertEquals(user.getBankCards().size(), 2);
    }

    @Test
    void addNewBankCard_NotValidBankCard_False() {
        //given
        final long cardNumber = 1234567890123456L;
        final String cardholderName = "  ";
        final int cvv = 1234;
        BankCardDTO bankCardDTO = BankCardDTO.builder()
                .cardNumber(cardNumber)
                .expiryDate("13/12")
                .cardholderName(cardholderName)
                .cvv(cvv)
                .build();
        User user = getUserWithValidNumberOfCards();

        //when
        boolean result = bankCardService.addNewBankCard(bankCardDTO, user);

        //then
        Assertions.assertFalse(result);
        Assertions.assertEquals(user.getBankCards().size(), 1);
    }

    @Test
    void addNewBankCard_NotAcceptableQuantityOfBankcards_False() {
        //given
        BankCardDTO bankCardDTO = getValidBankCardDTO();
        User user = User.builder()
                .bankCards(new ArrayList<>(
                        List.of(
                                BankCard.builder().build(),
                                BankCard.builder().build(),
                                BankCard.builder().build(),
                                BankCard.builder().build()
                        )
                ))
                .build();

        //when
        boolean result = bankCardService.addNewBankCard(bankCardDTO, user);

        //then
        Assertions.assertFalse(result);
        Assertions.assertEquals(user.getBankCards().size(), 4);
    }

    @Test
    void addNewBankCard_BankCardIsAlreadyExists_False() {
        //given
        BankCardDTO bankCardDTO = getValidBankCardDTO();
        BankCard bankCard = BankCard.builder()
                .cardNumber(bankCardDTO.getCardNumber())
                .expirationMonth(11)
                .expirationYear(29)
                .cardholderName(bankCardDTO.getCardholderName())
                .cvv(bankCardDTO.getCvv())
                .build();
        User user = getUserWithValidNumberOfCards();

        //when
        Mockito.when(bankCardRepository.findByCardNumber(bankCardDTO.getCardNumber())).thenReturn(bankCard);
        boolean result = bankCardService.addNewBankCard(bankCardDTO, user);

        //then
        Assertions.assertFalse(result);
        Assertions.assertEquals(user.getBankCards().size(), 1);
    }

    private BankCardDTO getValidBankCardDTO() {
        return BankCardDTO.builder()
                .cardNumber(3234567890123456L)
                .expiryDate("11/29")
                .cardholderName("Alexey Burak")
                .cvv(123)
                .build();
    }

    private User getUserWithValidNumberOfCards() {
        return User.builder()
                .bankCards(new ArrayList<>(
                        List.of(
                                BankCard.builder().build()
                        )
                ))
                .build();
    }

}