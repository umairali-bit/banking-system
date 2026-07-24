package com.umair.banking.validation.validator;

import com.umair.banking.validation.annotation.ValidName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NameValidator implements ConstraintValidator<ValidName, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;

        return value.chars().allMatch(ch ->
                Character.isLetter(ch) || ch == ' ');
    }
}
