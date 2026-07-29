package com.umair.banking.transaction.dto.response;

import com.umair.banking.account.enums.Currency;
import com.umair.banking.transaction.enums.TransactionStatus;
import com.umair.banking.transaction.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(

        Long transactionId,
        String transactionReference,
        Long sourceAccountId,
        Long destinationAccountId,
        BigDecimal sourceAmount,
        Currency sourceCurrency,
        BigDecimal destinationAmount,
        Currency destinationCurrency,
        TransactionType transactionType,
        TransactionStatus transactionStatus,
        LocalDateTime createdAt


) {
}
