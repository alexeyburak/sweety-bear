package by.bsuir.sweetybear.dto.address;

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
public class AddressDTO {
    String street;
    Integer houseNumber;
    Integer apartmentNumber;
    Integer caseNumber;
    Integer postalCode;
}
