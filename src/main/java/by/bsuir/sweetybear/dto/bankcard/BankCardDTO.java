package by.bsuir.sweetybear.dto.bankcard;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankCardDTO {
    Long cardNumber;
    String expiryDate;
    String cardholderName;
    Integer cvv;
}
