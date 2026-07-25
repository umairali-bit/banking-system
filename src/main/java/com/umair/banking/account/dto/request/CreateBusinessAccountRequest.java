package com.umair.banking.account.dto.request;


import com.umair.banking.account.enums.Currency;
import com.umair.banking.validation.annotation.ValidOpeningBalance;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateBusinessAccountRequest(

        @NotNull
        Long customerId,

        @NotNull
        String businessName,

        @NotNull
        Currency currency,

        @ValidOpeningBalance
        BigDecimal openingBalance

) {




}
