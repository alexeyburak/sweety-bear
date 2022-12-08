package by.bsuir.sweetybear.validator.impl;

import by.bsuir.sweetybear.validator.AbstractValidator;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public class CardNameValidatorImpl extends AbstractValidator {
    private static final String NAME_REGEX = "^[A-ZА-Я]{1}[A-zА-я]{2,29}$";

    @Override
    protected String getRegex() {
        return NAME_REGEX;
    }
}
