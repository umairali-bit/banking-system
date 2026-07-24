package com.umair.banking.validation.annotation;

import jakarta.validation.Payload;

public @interface ValidPhone {

    String message() default "Invalid phone number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
