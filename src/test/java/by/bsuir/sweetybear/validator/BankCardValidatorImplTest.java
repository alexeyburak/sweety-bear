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
    void cardNameValidatorTest() {
        //given
        final String correctName = "Alexey Burak";
        final String correctName2 = "burak alexey";
        final String incorrectName = "Alexey Alexey Alexey";
        final String incorrectName2 = "";

        //then
        Assertions.assertTrue(cardNameValidator.isValid(correctName));
        Assertions.assertTrue(cardNameValidator.isValid(correctName2));
        Assertions.assertFalse(cardNameValidator.isValid(incorrectName));
        Assertions.assertFalse(cardNameValidator.isValid(incorrectName2));
    }

    @Test
    void cardNumberValidator() {
        //given
        final String correctNumber = "3742454554001261";
        final String correctNumber2 = "3782822463100054";
        final String incorrectNumber = "123456789012345631344";
        final String incorrectNumber2 = "123456";

        //then
        Assertions.assertTrue(cardNumberValidator.isValid(correctNumber));
        Assertions.assertTrue(cardNumberValidator.isValid(correctNumber2));
        Assertions.assertFalse(cardNumberValidator.isValid(incorrectNumber));
        Assertions.assertFalse(cardNumberValidator.isValid(incorrectNumber2));
    }

    @Test
    void cvvValidator() {
        //given
        final String correctCvv = "123";
        final String correctCvv2 = "012";
        final String incorrectCvv = "2";
        final String incorrectCvv2 = "3131323";

        //then
        Assertions.assertTrue(cvvValidator.isValid(correctCvv));
        Assertions.assertTrue(cvvValidator.isValid(correctCvv2));
        Assertions.assertFalse(cvvValidator.isValid(incorrectCvv));
        Assertions.assertFalse(cvvValidator.isValid(incorrectCvv2));
    }

    @Test
    void monthValidator() {
        //given
        final String correctMonth = "7";
        final String correctMonth2 = "1";
        final String incorrectMonth = "0";
        final String incorrectMonth2 = "13";

        //then
        Assertions.assertTrue(monthValidator.isValid(correctMonth));
        Assertions.assertTrue(monthValidator.isValid(correctMonth2));
        Assertions.assertFalse(monthValidator.isValid(incorrectMonth));
        Assertions.assertFalse(monthValidator.isValid(incorrectMonth2));
    }

    @Test
    void yearValidator() {
        //given
        final String correctYear = "23";
        final String correctYear2 = "15";
        final String incorrectYear = "100";
        final String incorrectYear2 = "-2";

        //then
        Assertions.assertTrue(yearValidator.isValid(correctYear));
        Assertions.assertTrue(yearValidator.isValid(correctYear2));
        Assertions.assertFalse(yearValidator.isValid(incorrectYear));
        Assertions.assertFalse(yearValidator.isValid(incorrectYear2));
    }

    @Test
    void paymentSystemValidator() {
        //given
        final long correctPayment = 3742454554001261L;
        final long correctPayment2 =5742454554001261L;
        final long incorrectPayment = 6234567890123456L;
        final long incorrectPayment2 = 1234567890123456L;

        //then
        Assertions.assertTrue(paymentSystemValidator.isValid(correctPayment));
        Assertions.assertTrue(paymentSystemValidator.isValid(correctPayment2));
        Assertions.assertFalse(paymentSystemValidator.isValid(incorrectPayment));
        Assertions.assertFalse(paymentSystemValidator.isValid(incorrectPayment2));
    }
}
