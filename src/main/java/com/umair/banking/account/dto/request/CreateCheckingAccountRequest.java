package com.umair.banking.account.dto.request;

import com.umair.banking.account.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateCheckingAccountRequest(

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @NotNull
        @Positive
        BigDecimal openingBalance,

        @NotNull
        Currency currency
) {
}
