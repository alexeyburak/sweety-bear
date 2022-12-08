package by.bsuir.sweetybear.validator.impl;

import by.bsuir.sweetybear.validator.AbstractValidator;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public class MonthValidatorImpl extends AbstractValidator {
    private static final String MONTH_REGEX = "^([1-9]|1[012])$";

    @Override
    protected String getRegex() {
        return MONTH_REGEX;
    }
}
