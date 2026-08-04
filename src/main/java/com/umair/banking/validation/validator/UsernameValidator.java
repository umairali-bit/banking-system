package com.umair.banking.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.umair.banking.validation.annotation.Username;


public class UsernameValidator implements ConstraintValidator<Username, String>{

    private static final String USERNAME_PATTERN =   "^[a-zA-Z0-9._]{4,20}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isEmpty()) {
            return false;
        }

        return value.matches(USERNAME_PATTERN);
    }
}
