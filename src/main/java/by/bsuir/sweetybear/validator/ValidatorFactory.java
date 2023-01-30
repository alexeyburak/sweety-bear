package by.bsuir.sweetybear.validator;

import by.bsuir.sweetybear.validator.impl.address.*;
import by.bsuir.sweetybear.validator.impl.bankcard.*;
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
    private final PostalCodeValidatorImpl postalCodeValidator = new PostalCodeValidatorImpl();
    private final CaseNumberValidatorImpl caseNumberValidator = new CaseNumberValidatorImpl();
    private final ApartmentNumberValidatorImpl apartmentNumberValidator = new ApartmentNumberValidatorImpl();
    private final HouseNumberValidatorImpl houseNumberValidator = new HouseNumberValidatorImpl();
    private final StreetValidatorImpl streetValidator = new StreetValidatorImpl();
    private final AddressValidatorImpl addressValidator = new AddressValidatorImpl();
    private final PaymentSystemValidatorImpl paymentSystemValidator = new PaymentSystemValidatorImpl();

    public static ValidatorFactory getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        static final ValidatorFactory INSTANCE = new ValidatorFactory();
    }
}
