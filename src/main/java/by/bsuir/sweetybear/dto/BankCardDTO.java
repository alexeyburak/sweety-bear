package by.bsuir.sweetybear.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Data
@NoArgsConstructor
public class BankCardDTO {

    private Long cardNumber;
    private String expiryDate;
    private String cardholderName;
    private Integer cvv;
}
