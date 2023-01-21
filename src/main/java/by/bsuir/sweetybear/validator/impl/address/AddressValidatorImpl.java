package by.bsuir.sweetybear.validator.impl.address;

import by.bsuir.sweetybear.dto.AddressDTO;
import by.bsuir.sweetybear.validator.Validator;
import by.bsuir.sweetybear.validator.ValidatorFactory;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

public class AddressValidatorImpl implements Validator<AddressDTO> {

    @Override
    public boolean isValid(AddressDTO address) {
        return isAddressNotNull(address) && isAddressValid(address);
    }

    private boolean isAddressNotNull(AddressDTO address) {
        return address.getStreet() != null &&
                address.getHouseNumber() != null &&
                address.getApartmentNumber() != null &&
                address.getCaseNumber() != null &&
                address.getPostalCode() != null;
    }

    private boolean isAddressValid(AddressDTO address) {
        return ValidatorFactory.getInstance().getPostalCodeValidator().isValid(address.getPostalCode()) &&
                ValidatorFactory.getInstance().getCaseNumberValidator().isValid(address.getCaseNumber()) &&
                ValidatorFactory.getInstance().getApartmentNumberValidator().isValid(address.getApartmentNumber()) &&
                ValidatorFactory.getInstance().getHouseNumberValidator().isValid(address.getHouseNumber()) &&
                ValidatorFactory.getInstance().getStreetValidator().isValid(address.getStreet());
    }
}
