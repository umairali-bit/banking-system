package com.umair.banking.customer.dto.request;

import com.umair.banking.validation.annotation.ValidName;
import com.umair.banking.validation.annotation.ValidPhone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(

        @NotBlank(message = "First name is required")
        @ValidName
        String firstName,

        @NotBlank(message = "Last name is required")
        @ValidName
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Phone number is required")
        @ValidPhone
        String phoneNumber

) {
}
