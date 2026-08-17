package com.umair.banking.security.dto.request;

import com.umair.banking.validation.annotation.Password;
import com.umair.banking.validation.annotation.Username;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(

        @Username
        String username,

        @Email
        @NotBlank
        String email,

        @Password
        String password,

        @NotBlank(message = "Customer number is required")
        String customerNumber
) {
}
