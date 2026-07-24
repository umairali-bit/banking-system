package com.umair.banking.validation.annotation;

import com.umair.banking.validation.validator.NameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidName {

    String message() default "Name can only contain letters and spaces";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}