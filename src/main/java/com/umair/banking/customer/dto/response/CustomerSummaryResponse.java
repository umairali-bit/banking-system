package com.umair.banking.customer.dto.response;

public record CustomerSummaryResponse(

        Long id,
        String customerNumber,
        String fullName,
        String email,
        int totalAccounts
) {
}