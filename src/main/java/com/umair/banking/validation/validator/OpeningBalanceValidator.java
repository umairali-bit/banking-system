package com.umair.banking.validation.validator;

import com.umair.banking.account.dto.request.CreateSavingsAccountRequest;
import com.umair.banking.validation.annotation.ValidOpeningBalance;
import com.umair.banking.currency.service.CurrencyConversionService;
import com.umair.banking.validation.constants.ValidationConstant;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class OpeningBalanceValidator implements
        ConstraintValidator<ValidOpeningBalance, CreateSavingsAccountRequest> {

    private final CurrencyConversionService currencyConversionService;

    @Override
    public boolean isValid(
            CreateSavingsAccountRequest request,
            ConstraintValidatorContext context) {

        if (request == null) {
            return true;
        }

        BigDecimal amountInUsd = currencyConversionService.convertToUsd(
                request.openingBalance(),
                request.currency()
        );

        return amountInUsd.compareTo(
                ValidationConstant.MINIMUM_OPENING_BALANCE
        ) >= 0;
    }
}