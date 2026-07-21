package com.umair.banking.account.dto.response;

import com.umair.banking.account.enums.AccountStatus;
import com.umair.banking.account.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SavingsAccountResponse(

        Long id,
        String accountNumber,
        String customerName,
        BigDecimal balance,
        Currency currency,
        AccountStatus status,
        LocalDateTime createdAt
) {
}
