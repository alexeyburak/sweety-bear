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
    void apartmentNumberValidator_CorrectNumbers_True() {
        //given
        final int correctApartment = 123;
        final int correctApartment2 = 999;
        final int correctApartment3 = 1;
        final int correctApartment4 = 654;

        //then
        Assertions.assertTrue(apartmentNumberValidator.isValid(correctApartment));
        Assertions.assertTrue(apartmentNumberValidator.isValid(correctApartment2));
        Assertions.assertTrue(apartmentNumberValidator.isValid(correctApartment3));
        Assertions.assertTrue(apartmentNumberValidator.isValid(correctApartment4));
    }

    @Test
    void apartmentNumberValidator_IncorrectNumbers_False() {
        //given
        final int incorrectApartment = 0;
        final int incorrectApartment2 = -100;
        final int incorrectApartment3 = 1000;
        final int incorrectApartment4 = -5;

        //then
        Assertions.assertFalse(apartmentNumberValidator.isValid(incorrectApartment));
        Assertions.assertFalse(apartmentNumberValidator.isValid(incorrectApartment2));
        Assertions.assertFalse(apartmentNumberValidator.isValid(incorrectApartment3));
        Assertions.assertFalse(apartmentNumberValidator.isValid(incorrectApartment4));
    }

    @Test
    void caseNumberValidator_CorrectCases_True() {
        //given
        final int correctCase = 15;
        final int correctCase2 = 30;
        final int correctCase3 = 1;
        final int correctCase4 = 10;

        //then
        Assertions.assertTrue(caseNumberValidator.isValid(correctCase));
        Assertions.assertTrue(caseNumberValidator.isValid(correctCase2));
        Assertions.assertTrue(caseNumberValidator.isValid(correctCase3));
        Assertions.assertTrue(caseNumberValidator.isValid(correctCase4));
    }

    @Test
    void caseNumberValidator_IncorrectCases_False() {
        //given
        final int incorrectCase = 0;
        final int incorrectCase2 = -100;
        final int incorrectCase3 = 31;
        final int incorrectCase4 = -5;

        //then
        Assertions.assertFalse(caseNumberValidator.isValid(incorrectCase));
        Assertions.assertFalse(caseNumberValidator.isValid(incorrectCase2));
        Assertions.assertFalse(caseNumberValidator.isValid(incorrectCase3));
        Assertions.assertFalse(caseNumberValidator.isValid(incorrectCase4));
    }

    @Test
    void houseNumberValidator_CorrectHouses_True() {
        //given
        final int correctHouse = 88;
        final int correctHouse2 = 777;
        final int correctHouse3 = 1;
        final int correctHouse4 = 999;

        //then
        Assertions.assertTrue(houseNumberValidator.isValid(correctHouse));
        Assertions.assertTrue(houseNumberValidator.isValid(correctHouse2));
        Assertions.assertTrue(houseNumberValidator.isValid(correctHouse3));
        Assertions.assertTrue(houseNumberValidator.isValid(correctHouse4));
    }

    @Test
    void houseNumberValidator_IncorrectHouses_False() {
        //given
        final int incorrectHouse = 1000;
        final int incorrectHouse2 = -10;
        final int incorrectHouse3 = 0;
        final int incorrectHouse4 = -5;

        //then
        Assertions.assertFalse(houseNumberValidator.isValid(incorrectHouse));
        Assertions.assertFalse(houseNumberValidator.isValid(incorrectHouse2));
        Assertions.assertFalse(houseNumberValidator.isValid(incorrectHouse3));
        Assertions.assertFalse(houseNumberValidator.isValid(incorrectHouse4));
    }

    @Test
    void postalCodeValidator_CorrectCodes_True() {
        //given
        final int correctCode = 220048;
        final int correctCode2 = 220141;
        final int correctCode3 = 220077;
        final int correctCode4 = 220096;

        //then
        Assertions.assertTrue(postalCodeValidator.isValid(correctCode));
        Assertions.assertTrue(postalCodeValidator.isValid(correctCode2));
        Assertions.assertTrue(postalCodeValidator.isValid(correctCode3));
        Assertions.assertTrue(postalCodeValidator.isValid(correctCode4));
    }

    @Test
    void postalCodeValidator_IncorrectCodes_False() {
        //given
        final int incorrectCode = 220142;
        final int incorrectCode2 = 220000;
        final int incorrectCode3 = 0;
        final int incorrectCode4 = -5;
        final int incorrectCode5 = 2200240;

        //then
        Assertions.assertFalse(postalCodeValidator.isValid(incorrectCode));
        Assertions.assertFalse(postalCodeValidator.isValid(incorrectCode2));
        Assertions.assertFalse(postalCodeValidator.isValid(incorrectCode3));
        Assertions.assertFalse(postalCodeValidator.isValid(incorrectCode4));
        Assertions.assertFalse(postalCodeValidator.isValid(incorrectCode5));
    }

    @Test
    void streetValidator_CorrectStreet_True() {
        //when
        final String correctStreet = "Lenina";
        final String correctStreet2 = "Brovki";
        final String correctStreet3 = "Kirova";
        final String correctStreet4 = "Kalinina";

        //then
        Assertions.assertTrue(streetValidator.isValid(correctStreet));
        Assertions.assertTrue(streetValidator.isValid(correctStreet2));
        Assertions.assertTrue(streetValidator.isValid(correctStreet3));
        Assertions.assertTrue(streetValidator.isValid(correctStreet4));
    }

    @Test
    void streetValidator_IncorrectStreet_False() {
        //when
        final String incorrectStreet = "Lenina Lenina Lenina";
        final String incorrectStreet2 = "";
        final String incorrectStreet3 = "Lenina 123";
        final String incorrectStreet4 = "   ";

        //then
        Assertions.assertFalse(streetValidator.isValid(incorrectStreet));
        Assertions.assertFalse(streetValidator.isValid(incorrectStreet2));
        Assertions.assertFalse(streetValidator.isValid(incorrectStreet3));
        Assertions.assertFalse(streetValidator.isValid(incorrectStreet4));
    }
}
