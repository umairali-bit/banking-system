package com.umair.banking.account.dto.response;

import com.umair.banking.account.enums.AccountStatus;
import com.umair.banking.account.enums.AccountType;
import com.umair.banking.account.enums.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BusinessAccountResponse(

        Long id,
        String accountNumber,
        String businessName,
        String registrationNumber,

        Long customerId,
        String customerNumber,
        String customerName,

        AccountType accountType,
        BigDecimal balance,
        Currency currency,
        BigDecimal creditLimit,
        AccountStatus status,



        LocalDateTime createdAt
) {
}
