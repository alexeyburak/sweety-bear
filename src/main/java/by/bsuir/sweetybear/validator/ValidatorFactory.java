package by.bsuir.sweetybear.validator;

import by.bsuir.sweetybear.validator.impl.*;
import lombok.Data;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Data
public class ValidatorFactory {

    private final CardNameValidatorImpl cardNameValidator = new CardNameValidatorImpl();
    private final CardNumberValidatorImpl cardNumberValidator = new CardNumberValidatorImpl();
    private final CvvValidatorImpl cvvValidator = new CvvValidatorImpl();
    private final MonthValidatorImpl monthValidator = new MonthValidatorImpl();
    private final YearValidatorImpl yearValidator = new YearValidatorImpl();

    public static ValidatorFactory getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        static final ValidatorFactory INSTANCE = new ValidatorFactory();
    }
}
