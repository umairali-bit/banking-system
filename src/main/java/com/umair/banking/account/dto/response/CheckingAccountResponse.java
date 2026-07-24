package com.umair.banking.account.dto.response;

import com.umair.banking.account.enums.AccountStatus;
import com.umair.banking.account.enums.AccountType;
import com.umair.banking.account.enums.Currency;
import com.umair.banking.customer.entity.Customer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CheckingAccountResponse(

        Long id,
        String accountNumber,

        Long customerId,
        String customerNumber,
        String customerName,

        AccountType accountType,
        BigDecimal balance,
        Currency currency,
        BigDecimal overdraftLimit,
        AccountStatus status,
        LocalDateTime createdAt






) {
}
