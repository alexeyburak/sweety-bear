package by.bsuir.sweetybear.validator.impl.bankcard;

import by.bsuir.sweetybear.validator.AbstractValidator;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public class CardNameValidatorImpl extends AbstractValidator {
    private static final String NAME_REGEX = "^((?:[A-Za-z]+ ?){1,3})$";

    @Override
    protected String getRegex() {
        return NAME_REGEX;
    }
}
