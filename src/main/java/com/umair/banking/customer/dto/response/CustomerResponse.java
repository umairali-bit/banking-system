package com.umair.banking.customer.dto.response;

import java.time.LocalDateTime;

public record CustomerResponse(

        Long id,
        String customerNumber,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        LocalDateTime createdAt
) {
}