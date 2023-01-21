package by.bsuir.sweetybear.validator.impl.address;

import by.bsuir.sweetybear.validator.Validator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

public class PostalCodeValidatorImpl implements Validator<Integer> {

    // Belarus, Minsk postal codes
    private static final Integer[] CITY_POSTAL_CODES = {
            220002, 220004, 220005, 220006, 220007, 220008, 220010, 220012, 220013,
            220014, 220015, 220016, 220017, 220018, 220019, 220020, 220021, 220024,
            220025, 220026, 220027, 220028, 220029, 220030, 220031, 220033, 220034,
            220035, 220036, 220037, 220039, 220040, 220042, 220043, 220044, 220047,
            220048, 220049, 220050, 220051, 220053, 220056, 220057, 220060, 220066,
            220068, 220070, 220072, 220073, 220075, 220077, 220079, 220082, 220085,
            220086, 220088, 220089, 220090, 220092, 220093, 220094, 220095, 220096,
            220099, 220100, 220101, 220102, 220103, 220104, 220107, 220108, 220109,
            220112, 220113, 220114, 220116, 220117, 220118, 220119, 220121, 220123,
            220124, 220125, 220126, 220131, 220136, 220137, 220138, 220140, 220141
    };

    @Override
    public boolean isValid(Integer expression) {
        return searchExpressionInArray(expression);
    }

    private boolean searchExpressionInArray(Integer postalCode) {
        Set<Integer> asSet = convertArrayToSet();
        return asSet.contains(postalCode);
    }

    private static Set<Integer> convertArrayToSet() {
        Set<Integer> setWithArray = new HashSet<>();
        Collections.addAll(setWithArray, CITY_POSTAL_CODES);
        return setWithArray;
    }

}
