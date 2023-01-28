package by.bsuir.sweetybear.validator.impl.bankcard;

import by.bsuir.sweetybear.validator.Validator;

import static by.bsuir.sweetybear.validator.impl.bankcard.PaymentSystemStartCodes.*;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

public class PaymentSystemValidatorImpl implements Validator<Long> {

    @Override
    public boolean isValid(Long expression) {
        return isPaymentSystemValid(expression);
    }

    public boolean isPaymentSystemValid(Long cardNumber) {
        var expressionFirstChar = cardNumber.toString().substring(0, 1);

        return isAmericanExpress(expressionFirstChar) ||
                isMasterCard(expressionFirstChar) ||
                isVisa(expressionFirstChar);
    }

    private boolean isMasterCard(String firstChar) {
        for (String s : MASTERCARD_START_WITH) {
            if (s.equals(firstChar)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAmericanExpress(String firstChar) {
        return firstChar.equals(AMERICAN_EXPRESS_START_WITH);
    }

    private boolean isVisa(String firstChar) {
        return firstChar.equals(VISA_START_WITH);
    }
}
