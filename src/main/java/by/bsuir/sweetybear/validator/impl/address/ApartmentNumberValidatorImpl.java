package by.bsuir.sweetybear.validator.impl.address;

import by.bsuir.sweetybear.validator.Validator;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

public class ApartmentNumberValidatorImpl implements Validator<Integer> {

    private static final int MIN_APARTMENT_NUMBER = 1;
    private static final int MAX_APARTMENT_NUMBER = 999;

    @Override
    public boolean isValid(Integer apartmentNumber) {
        return apartmentNumber >= MIN_APARTMENT_NUMBER && apartmentNumber <= MAX_APARTMENT_NUMBER;
    }
}

