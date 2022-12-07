package by.bsuir.sweetybear.annotation;

import by.bsuir.sweetybear.validator.PasswordMatchValidator;

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
@Constraint(validatedBy= PasswordMatchValidator.class)
@Documented
public @interface PasswordMatch {
    String message() default "Passwords should match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}