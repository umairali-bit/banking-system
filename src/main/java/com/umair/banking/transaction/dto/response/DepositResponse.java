package com.umair.banking.transaction.dto.response;

import com.umair.banking.account.enums.Currency;
import com.umair.banking.transaction.enums.TransactionStatus;
import com.umair.banking.transaction.enums.TransactionType;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DepositResponse(

        Long transactionId,
        String transactionReference,
        Long accountId,
        BigDecimal amount,
        Currency currency,
        BigDecimal newBalance,
        TransactionType transactionType,
        TransactionStatus transactionStatus,
        LocalDateTime createdAt





) {
}
