package com.umair.banking.validation.annotation;

import com.umair.banking.validation.validator.UsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Username {

    String message() default  "Username must be 4-20 characters and contain only letters, numbers, underscores, or periods.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
