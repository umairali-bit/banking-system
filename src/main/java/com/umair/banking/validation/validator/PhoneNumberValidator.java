package com.umair.banking.validation.validator;

import com.umair.banking.validation.annotation.ValidPhone;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhone, String> {


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;

        }
        String phone = value.replace("[\\s()-]", "");

        if (phone.startsWith("+")) {
            phone = phone.substring(1);
        }

        if(!phone.chars().allMatch(Character::isDigit)) {
            return false;
        }

        return phone.length() >= 10 && phone.length() <= 15;
    }
}
