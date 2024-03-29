package by.bsuir.sweetybear.validator.impl.user;

import by.bsuir.sweetybear.annotation.ForgotPasswordMatch;
import by.bsuir.sweetybear.dto.user.UserChangePasswordDTO;
import by.bsuir.sweetybear.validator.ErrorMessage;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public class ForgotPasswordMatchConstraintValidatorImpl implements ConstraintValidator<ForgotPasswordMatch, UserChangePasswordDTO> {

    @Override
    public void initialize(ForgotPasswordMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserChangePasswordDTO userDTO, ConstraintValidatorContext constraintValidatorContext) {
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
