package by.bsuir.sweetybear.validator.impl.user;

import by.bsuir.sweetybear.annotation.UsernameValid;
import by.bsuir.sweetybear.validator.ErrorMessage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public class UsernameConstraintValidatorImpl implements ConstraintValidator<UsernameValid, String> {

    @Override
    public void initialize(UsernameValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final String username, ConstraintValidatorContext constraintValidatorContext) {
        final int USERNAME_LENGTH_MIN = 2;
        final int USERNAME_LENGTH_MAX = 15;

        boolean isUsernameLengthValid = username.length() > USERNAME_LENGTH_MIN && username.length() < USERNAME_LENGTH_MAX;
        if (!isUsernameLengthValid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(ErrorMessage.USER_NAME_LENGTH_ERROR)
                    .addConstraintViolation();
        }
        return isUsernameLengthValid;
    }
}
