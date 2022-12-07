package by.bsuir.sweetybear.validator;

import by.bsuir.sweetybear.annotation.PasswordMatch;
import by.bsuir.sweetybear.dto.UserDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, UserDTO> {

    @Override
    public void initialize(PasswordMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserDTO userDTO, ConstraintValidatorContext constraintValidatorContext) {
        boolean isUserPasswordMatchingConfirmPassword = userDTO.getPassword().equals(userDTO.getConfirmPassword());
        if (!isUserPasswordMatchingConfirmPassword) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Passwords should match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }
        return isUserPasswordMatchingConfirmPassword;
    }
}
