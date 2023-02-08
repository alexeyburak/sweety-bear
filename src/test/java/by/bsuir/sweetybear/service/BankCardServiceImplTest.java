package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.dto.bankcard.AccessibleBankCardDTO;
import by.bsuir.sweetybear.model.BankCard;
import by.bsuir.sweetybear.repository.BankCardRepository;
import by.bsuir.sweetybear.service.impl.BankCardServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
