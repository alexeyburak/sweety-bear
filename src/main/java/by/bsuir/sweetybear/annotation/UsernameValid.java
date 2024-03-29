package by.bsuir.sweetybear.annotation;

import by.bsuir.sweetybear.validator.ErrorMessage;
import by.bsuir.sweetybear.validator.impl.user.UsernameConstraintValidatorImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = UsernameConstraintValidatorImpl.class)
@Documented
public @interface UsernameValid {
    String message() default ErrorMessage.USER_INVALID_USERNAME_INPUT;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
