package by.bsuir.sweetybear.validator.impl;

import by.bsuir.sweetybear.annotation.ForgotPasswordMatch;
import by.bsuir.sweetybear.dto.UserForgotPasswordDTO;
import by.bsuir.sweetybear.validator.ErrorMessage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public class ForgotPasswordMatchValidatorImpl implements ConstraintValidator<ForgotPasswordMatch, UserForgotPasswordDTO> {

    @Override
    public void initialize(ForgotPasswordMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserForgotPasswordDTO userDTO, ConstraintValidatorContext constraintValidatorContext) {
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
