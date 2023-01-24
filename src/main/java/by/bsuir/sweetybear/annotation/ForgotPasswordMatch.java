package by.bsuir.sweetybear.annotation;

import by.bsuir.sweetybear.validator.ErrorMessage;
import by.bsuir.sweetybear.validator.impl.user.ForgotPasswordMatchConstraintValidatorImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy= ForgotPasswordMatchConstraintValidatorImpl.class)
@Documented
public @interface ForgotPasswordMatch {
    String message() default ErrorMessage.PASSWORDS_NOT_MATCH;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
