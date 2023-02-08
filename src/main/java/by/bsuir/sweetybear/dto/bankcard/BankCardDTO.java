package by.bsuir.sweetybear.dto.bankcard;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankCardDTO {
    Long cardNumber;
    String expiryDate;
    String cardholderName;
    Integer cvv;
}
