package com.umair.banking.validation.annotation;

import com.umair.banking.validation.validator.UsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

    String message() default
            "Password must contain at least 8 characters, one uppercase letter, one lowercase letter, one number, and one special character.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
