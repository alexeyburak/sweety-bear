package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.dto.bankcard.AccessibleBankCardDTO;
import by.bsuir.sweetybear.dto.bankcard.BankCardDTO;
import by.bsuir.sweetybear.model.BankCard;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.CardPaymentSystem;
import by.bsuir.sweetybear.repository.BankCardRepository;
import by.bsuir.sweetybear.service.impl.BankCardServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                .bankCards(List.of(
                                BankCard.builder().build()
                        )
                )
                .build();
    }

    @Test
    void setPaymentSystemToBankCard_ValidBankCard_ShouldSetPaymentSystem() {
        //given
        final long visaCardNumber = 4234567890123456L;
        final long masterCardNumber = 5234567890123456L;
        final long americanExpressCardNumber = 3234567890123456L;
        BankCard visaBankCard = BankCard.builder()
                .cardNumber(visaCardNumber)
                .build();
        BankCard masterCardBankCard = BankCard.builder()
                .cardNumber(masterCardNumber)
                .build();
        BankCard americanExpressBankCard = BankCard.builder()
                .cardNumber(americanExpressCardNumber)
                .build();

        //when
        bankCardService.setPaymentSystemToBankCard(visaBankCard);
        bankCardService.setPaymentSystemToBankCard(masterCardBankCard);
        bankCardService.setPaymentSystemToBankCard(americanExpressBankCard);

        //then
        Assertions.assertNotNull(visaBankCard);
        Assertions.assertNotNull(masterCardBankCard);
        Assertions.assertNotNull(americanExpressBankCard);
        Assertions.assertEquals(CardPaymentSystem.VISA, visaBankCard.getPaymentSystem());
        Assertions.assertEquals(CardPaymentSystem.MASTERCARD, masterCardBankCard.getPaymentSystem());
        Assertions.assertEquals(CardPaymentSystem.AMERICAN_EXPRESS, americanExpressBankCard.getPaymentSystem());
    }

    @Test
    void isBankCardValid_ValidCard_True() {
        //given
        BankCard bankCard = BankCard.builder()
                .cardNumber(3234567890123456L)
                .expirationMonth(12)
                .expirationYear(28)
                .cardholderName("Alexey Burak")
                .cvv(122)
                .build();

        //when
        boolean result = bankCardService.isBankCardValid(bankCard);

        //then
        Assertions.assertNotNull(bankCard);
        Assertions.assertTrue(result);

    }

    @Test
    void isBankCardValid_NotValidCard_False() {
        //given
        BankCard bankCard = BankCard.builder()
                .cardNumber(1234567890123456L)
                .expirationMonth(9)
                .expirationYear(13)
                .cardholderName("Alexey Burak")
                .cvv(12)
                .build();

        //when
        boolean result = bankCardService.isBankCardValid(bankCard);

        //then
        Assertions.assertNotNull(bankCard);
        Assertions.assertFalse(result);
    }

    @Test
    void isEnoughMoney_ValidAmount_True() {
        //given
        BigDecimal balance = new BigDecimal(100);
        BigDecimal balance2 = new BigDecimal(50);
        BigDecimal price = new BigDecimal(50);

        //when
        boolean result = bankCardService.isEnoughMoney(balance, price);
        boolean result2 = bankCardService.isEnoughMoney(balance2, price);

        //then
        Assertions.assertTrue(result);
        Assertions.assertTrue(result2);
    }

    @Test
    void isEnoughMoney_NotValidAmount_False() {
        //given
        BigDecimal balance = new BigDecimal(10);
        BigDecimal price = new BigDecimal(50);

        //when
        boolean result = bankCardService.isEnoughMoney(balance, price);

        //then
        Assertions.assertFalse(result);
    }

    @Test
    void calculateNewBalance_BigDecimalValues_ShouldReturnSubtractBalance() {
        //given
        BigDecimal balance = new BigDecimal("100");
        BigDecimal balance2 = new BigDecimal("50");
        BigDecimal balance3 = new BigDecimal("52.24");
        BigDecimal price = new BigDecimal("50");

        //when
        BigDecimal result = bankCardService.calculateNewBalance(balance, price);
        BigDecimal result2 = bankCardService.calculateNewBalance(balance2, price);
        BigDecimal result3 = bankCardService.calculateNewBalance(balance3, price);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result2);
        Assertions.assertNotNull(result3);
        Assertions.assertEquals(new BigDecimal(50), result);
        Assertions.assertEquals(new BigDecimal(0), result2);
        Assertions.assertEquals(new BigDecimal("2.24"), result3);
    }

    @Test
    void getBankCardById_ValidId_ShouldReturnBankCard() {
        //given
        BankCard bankCard = new BankCard();
        bankCard.setId(1L);

        //when
        Mockito.when(bankCardRepository.findById(1L)).thenReturn(Optional.of(bankCard));
        BankCard result = bankCardService.getBankCardById(1L);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bankCard, result);
    }

}
