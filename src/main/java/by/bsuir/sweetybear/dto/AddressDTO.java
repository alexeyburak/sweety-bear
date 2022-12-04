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
public class AddressDTO {

    private String street;
    private Integer apartmentNumber;
    private Integer houseNumber;
    private Integer caseNumber;
    private Integer postCode;
}
