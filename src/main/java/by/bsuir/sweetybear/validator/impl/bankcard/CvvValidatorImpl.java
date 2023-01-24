package by.bsuir.sweetybear.validator.impl.bankcard;

import by.bsuir.sweetybear.validator.AbstractValidator;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public class CvvValidatorImpl extends AbstractValidator {
    private static final String CVV_REGEX = "^[0-9]{3}$";

    @Override
    protected String getRegex() {
        return CVV_REGEX;
    }
}
