package by.bsuir.sweetybear.validator.impl.address;

import by.bsuir.sweetybear.validator.Validator;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

public class HouseNumberValidatorImpl implements Validator<Integer> {

    private static final int MIN_HOUSE_NUMBER = 1;
    private static final int MAX_HOUSE_NUMBER = 999;

    @Override
    public boolean isValid(Integer houseNumber) {
        return houseNumber >= MIN_HOUSE_NUMBER && houseNumber <= MAX_HOUSE_NUMBER;
    }
}

