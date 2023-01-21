package by.bsuir.sweetybear.validator.impl.address;

import by.bsuir.sweetybear.validator.AbstractValidator;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

public class StreetValidatorImpl extends AbstractValidator {

    private static final String STREET_REGEX = "^\\w+((?:[A-Za-z]+ ?){1,2})$";

    @Override
    protected String getRegex() {
        return STREET_REGEX;
    }
}
