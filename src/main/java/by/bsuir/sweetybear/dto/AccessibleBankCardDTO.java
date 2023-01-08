package by.bsuir.sweetybear.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccessibleBankCardDTO {
    Long id;
    String cardNumber;
    String expiryDate;
}
