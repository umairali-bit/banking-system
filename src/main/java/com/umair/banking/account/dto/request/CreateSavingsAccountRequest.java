package com.umair.banking.account.dto.request;


import com.umair.banking.account.enums.Currency;
import com.umair.banking.customer.entity.Customer;
import com.umair.banking.validation.annotation.ValidOpeningBalance;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@ValidOpeningBalance
public record CreateSavingsAccountRequest (


        @NotNull
        Long customerId,

        @NotNull
        @Positive
        BigDecimal openingBalance,

        @NotNull
        Currency currency
) {


}
