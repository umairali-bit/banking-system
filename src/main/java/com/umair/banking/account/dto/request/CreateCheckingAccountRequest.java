package com.umair.banking.account.dto.request;

import com.umair.banking.account.enums.Currency;
import com.umair.banking.customer.entity.Customer;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateCheckingAccountRequest(


        @NotNull
        Long customerId,

        @NotNull
        @Positive
        BigDecimal openingBalance,

        @NotNull
        Currency currency
) {
}
