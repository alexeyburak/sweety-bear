package by.bsuir.sweetybear.validator;

import by.bsuir.sweetybear.validator.impl.bankcard.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Feb 2023
 */

public class BankCardValidatorImplTest {
    private static CardNameValidatorImpl cardNameValidator;
    private static CardNumberValidatorImpl cardNumberValidator;
    private static CvvValidatorImpl cvvValidator;
    private static MonthValidatorImpl monthValidator;
    private static YearValidatorImpl yearValidator;
    private static PaymentSystemValidatorImpl paymentSystemValidator;

    @BeforeAll
    static void init() {
        cardNameValidator = new CardNameValidatorImpl();
        cardNumberValidator = new CardNumberValidatorImpl();
        cvvValidator = new CvvValidatorImpl();
        monthValidator = new MonthValidatorImpl();
        yearValidator = new YearValidatorImpl();
        paymentSystemValidator = new PaymentSystemValidatorImpl();
    }

    @Test
    void cardNameValidator_CorrectName_True() {
        //given
        final String correctName = "Alexey Burak";
        final String correctName2 = "burak alexey";
        final String correctName3 = "Bill Gates";
        final String correctName4 = "Arkady Dobkin";

        //then
        Assertions.assertTrue(cardNameValidator.isValid(correctName));
        Assertions.assertTrue(cardNameValidator.isValid(correctName2));
        Assertions.assertTrue(cardNameValidator.isValid(correctName3));
        Assertions.assertTrue(cardNameValidator.isValid(correctName4));
    }

    @Test
    void cardNameValidator_IncorrectName_False() {
        //given
        final String incorrectName = "Alexey Alexey Alexey";
        final String incorrectName2 = "";
        final String incorrectName3 = "    ";
        final String incorrectName4 = ".?";
        final String incorrectName5 = "$ ";
        final String incorrectName6 = "*()";

        //then
        Assertions.assertFalse(cardNameValidator.isValid(incorrectName));
        Assertions.assertFalse(cardNameValidator.isValid(incorrectName2));
        Assertions.assertFalse(cardNameValidator.isValid(incorrectName3));
        Assertions.assertFalse(cardNameValidator.isValid(incorrectName4));
        Assertions.assertFalse(cardNameValidator.isValid(incorrectName5));
        Assertions.assertFalse(cardNameValidator.isValid(incorrectName6));
    }

    @Test
    void cardNumberValidator_CorrectNumber_True() {
        //given
        final String correctNumber = "3742454554001261";
        final String correctNumber2 = "3782822463100054";

        //then
        Assertions.assertTrue(cardNumberValidator.isValid(correctNumber));
        Assertions.assertTrue(cardNumberValidator.isValid(correctNumber2));
    }

    @Test
    void cardNumberValidator_IncorrectNumber_False() {
        //given
        final String incorrectNumber = "123456789012345631344";
        final String incorrectNumber2 = "123456";
        final String incorrectNumber3 = "  ";
        final String incorrectNumber4 = "12345678901234567890123456789012345678901234567890123456789012345678901234567890";
        final String incorrectNumber5 = "";

        //then
        Assertions.assertFalse(cardNumberValidator.isValid(incorrectNumber));
        Assertions.assertFalse(cardNumberValidator.isValid(incorrectNumber2));
        Assertions.assertFalse(cardNumberValidator.isValid(incorrectNumber3));
        Assertions.assertFalse(cardNumberValidator.isValid(incorrectNumber4));
        Assertions.assertFalse(cardNumberValidator.isValid(incorrectNumber5));
    }

    @Test
    void cvvValidator_CorrectCvv_True() {
        //given
        final String correctCvv = "123";
        final String correctCvv2 = "012";

        //then
        Assertions.assertTrue(cvvValidator.isValid(correctCvv));
        Assertions.assertTrue(cvvValidator.isValid(correctCvv2));
    }

    @Test
    void cvvValidator_IncorrectCvv_False() {
        //given
        final String incorrectCvv = "2";
        final String incorrectCvv2 = "3131323";
        final String incorrectCvv3 = "  ";
        final String incorrectCvv4 = "";
        final String incorrectCvv5 = "1234";

        //then
        Assertions.assertFalse(cvvValidator.isValid(incorrectCvv));
        Assertions.assertFalse(cvvValidator.isValid(incorrectCvv2));
        Assertions.assertFalse(cvvValidator.isValid(incorrectCvv3));
        Assertions.assertFalse(cvvValidator.isValid(incorrectCvv4));
        Assertions.assertFalse(cvvValidator.isValid(incorrectCvv5));
    }

    @Test
    void monthValidator_CorrectMonth_True() {
        //given
        final String correctMonth = "7";
        final String correctMonth2 = "1";

        //then
        Assertions.assertTrue(monthValidator.isValid(correctMonth));
        Assertions.assertTrue(monthValidator.isValid(correctMonth2));
    }

    @Test
    void monthValidator_IncorrectMonth_False() {
        //given
        final String incorrectMonth = "0";
        final String incorrectMonth2 = "13";
        final String incorrectMonth3 = "  ";
        final String incorrectMonth4 = "";
        final String incorrectMonth5 = "-5";

        //then
        Assertions.assertFalse(monthValidator.isValid(incorrectMonth));
        Assertions.assertFalse(monthValidator.isValid(incorrectMonth2));
        Assertions.assertFalse(monthValidator.isValid(incorrectMonth3));
        Assertions.assertFalse(monthValidator.isValid(incorrectMonth4));
        Assertions.assertFalse(monthValidator.isValid(incorrectMonth5));
    }

    @Test
    void yearValidator_CorrectYear_True() {
        //given
        final String correctYear = "23";
        final String correctYear2 = "15";

        //then
        Assertions.assertTrue(yearValidator.isValid(correctYear));
        Assertions.assertTrue(yearValidator.isValid(correctYear2));
    }

    @Test
    void yearValidator_IncorrectYear_False() {
        //given
        final String incorrectYear = "100";
        final String incorrectYear2 = "-2";
        final String incorrectYear3 = "  ";
        final String incorrectYear4 = "";
        final String incorrectYear5 = "0";

        //then
        Assertions.assertFalse(yearValidator.isValid(incorrectYear));
        Assertions.assertFalse(yearValidator.isValid(incorrectYear2));
        Assertions.assertFalse(yearValidator.isValid(incorrectYear3));
        Assertions.assertFalse(yearValidator.isValid(incorrectYear4));
        Assertions.assertFalse(yearValidator.isValid(incorrectYear5));
    }

    @Test
    void paymentSystemValidator_CorrectPayment_True() {
        //given
        final long correctPayment = 3742454554001261L;
        final long correctPayment2 = 5742454554001261L;

        //then
        Assertions.assertTrue(paymentSystemValidator.isValid(correctPayment));
        Assertions.assertTrue(paymentSystemValidator.isValid(correctPayment2));
    }

    @Test
    void paymentSystemValidator_IncorrectPayment_False() {
        //given
        final long incorrectPayment = 6234567890123456L;
        final long incorrectPayment2 = 1234567890123456L;
        final long incorrectPayment3 = 7234567890123456L;
        final long incorrectPayment4 = 8234567890123456L;

        //then
        Assertions.assertFalse(paymentSystemValidator.isValid(incorrectPayment));
        Assertions.assertFalse(paymentSystemValidator.isValid(incorrectPayment2));
        Assertions.assertFalse(paymentSystemValidator.isValid(incorrectPayment3));
        Assertions.assertFalse(paymentSystemValidator.isValid(incorrectPayment4));
    }
}
