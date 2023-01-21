package by.bsuir.sweetybear.validator.impl.address;

import by.bsuir.sweetybear.validator.Validator;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

public class CaseNumberValidatorImpl implements Validator<Integer> {

    private static final int MIN_CASE_NUMBER = 1;
    private static final int MAX_CASE_NUMBER = 30;

    @Override
    public boolean isValid(Integer expression) {
        return expression >= MIN_CASE_NUMBER && expression <= MAX_CASE_NUMBER;
    }

}
