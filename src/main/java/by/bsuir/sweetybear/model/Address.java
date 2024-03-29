package by.bsuir.sweetybear.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Locale;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Entity
@Table(name = "address")
@Data
public class Address extends IdentifiedModel {

    @Column(name = "street")
    private String street;
    @Column(name = "apartment_number")
    private Integer apartmentNumber;
    @Column(name = "house_number")
    private Integer houseNumber;
    @Column(name = "case_number")
    private Integer caseNumber;
    @Column(name = "post_code")
    private Integer postCode;

    @Override
    public String toString() {
        return street.toUpperCase(Locale.ENGLISH) + "/" + caseNumber + ", "
                + houseNumber + ", " + apartmentNumber
                + ", " + postCode;
    }
}
