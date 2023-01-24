package by.bsuir.sweetybear.validator.impl.bankcard;

import by.bsuir.sweetybear.validator.AbstractValidator;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public class CardNumberValidatorImpl extends AbstractValidator {
    private static final String CARD_NUMBER_REGEX = "^[0-9]{16}$";

    @Override
    protected String getRegex() {
        return CARD_NUMBER_REGEX;
    }
}