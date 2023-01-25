package by.bsuir.sweetybear.validator.impl.user;

import by.bsuir.sweetybear.annotation.PasswordMatch;
import by.bsuir.sweetybear.dto.UserRegistrationDTO;
import by.bsuir.sweetybear.validator.ErrorMessage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public class PasswordMatchConstraintValidatorImpl implements ConstraintValidator<PasswordMatch, UserRegistrationDTO> {

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserRegistrationDTO userDTO, ConstraintValidatorContext constraintValidatorContext) {
        boolean isUserPasswordMatchingConfirmPassword = userDTO.getPassword().equals(userDTO.getConfirmPassword());
        if (!isUserPasswordMatchingConfirmPassword) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(ErrorMessage.PASSWORDS_NOT_MATCH)
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }
        return isUserPasswordMatchingConfirmPassword;
    }
}
