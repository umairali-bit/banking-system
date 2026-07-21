package com.umair.banking.validation.annotation;

import com.umair.banking.validation.validator.OpeningBalanceValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OpeningBalanceValidator.class)
@Documented
public @interface ValidOpeningBalance {

    String message() default "Opening balance must be at least 100 USD.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
