package by.bsuir.sweetybear.validator;

import by.bsuir.sweetybear.validator.impl.address.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Feb 2023
 */

public class AddressValidatorImplTest {
    private static ApartmentNumberValidatorImpl apartmentNumberValidator;
    private static CaseNumberValidatorImpl caseNumberValidator;
    private static HouseNumberValidatorImpl houseNumberValidator;
    private static PostalCodeValidatorImpl postalCodeValidator;
    private static StreetValidatorImpl streetValidator;

    @BeforeAll
    static void init() {
        apartmentNumberValidator = new ApartmentNumberValidatorImpl();
        caseNumberValidator = new CaseNumberValidatorImpl();
        houseNumberValidator = new HouseNumberValidatorImpl();
        postalCodeValidator = new PostalCodeValidatorImpl();
        streetValidator = new StreetValidatorImpl();
    }

    @Test
    void apartmentNumberTest() {
        //given
        final int correctApartment = 123;
        final int correctApartment2 = 999;
        final int incorrectApartment = 0;
        final int incorrectApartment2 = -100;

        //then
        Assertions.assertTrue(apartmentNumberValidator.isValid(correctApartment));
        Assertions.assertTrue(apartmentNumberValidator.isValid(correctApartment2));
        Assertions.assertFalse(apartmentNumberValidator.isValid(incorrectApartment));
        Assertions.assertFalse(apartmentNumberValidator.isValid(incorrectApartment2));
    }

    @Test
    void caseNumberValidatorTest() {
        //given
        final int correctCase = 15;
        final int correctCase2 = 30;
        final int incorrectCase = 0;
        final int incorrectCase2 = -100;

        //then
        Assertions.assertTrue(caseNumberValidator.isValid(correctCase));
        Assertions.assertTrue(caseNumberValidator.isValid(correctCase2));
        Assertions.assertFalse(caseNumberValidator.isValid(incorrectCase));
        Assertions.assertFalse(caseNumberValidator.isValid(incorrectCase2));
    }

    @Test
    void houseNumberValidatorTest() {
        //given
        final int correctHouse = 2;
        final int correctHouse2 = 30;
        final int incorrectHouse = 1000;
        final int incorrectHouse2 = -10;

        //then
        Assertions.assertTrue(houseNumberValidator.isValid(correctHouse));
        Assertions.assertTrue(houseNumberValidator.isValid(correctHouse2));
        Assertions.assertFalse(houseNumberValidator.isValid(incorrectHouse));
        Assertions.assertFalse(houseNumberValidator.isValid(incorrectHouse2));
    }

    @Test
    void postalCodeValidatorTest() {
        //given
        final int correctCode = 220048;
        final int correctCode2 = 220141;
        final int incorrectCode = 220142;
        final int incorrectCode2 = 220000;

        //then
        Assertions.assertTrue(postalCodeValidator.isValid(correctCode));
        Assertions.assertTrue(postalCodeValidator.isValid(correctCode2));
        Assertions.assertFalse(postalCodeValidator.isValid(incorrectCode));
        Assertions.assertFalse(postalCodeValidator.isValid(incorrectCode2));
    }

    @Test
    void streetValidatorTest() {
        //when
        final String correctStreet = "Lenina";
        final String correctStreet2 = "Brovki";
        final String incorrectStreet = "Lenina Lenina Lenina";
        final String incorrectStreet2 = "";

        //then
        Assertions.assertTrue(streetValidator.isValid(correctStreet));
        Assertions.assertTrue(streetValidator.isValid(correctStreet2));
        Assertions.assertFalse(streetValidator.isValid(incorrectStreet));
        Assertions.assertFalse(streetValidator.isValid(incorrectStreet2));
    }
}
