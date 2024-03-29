package by.bsuir.sweetybear.validator.impl.bankcard;

import by.bsuir.sweetybear.validator.AbstractValidator;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public class YearValidatorImpl extends AbstractValidator {
    private static final String YEAR_REGEX = "^[0-9]{2}$";

    @Override
    protected String getRegex() {
        return YEAR_REGEX;
    }
}
