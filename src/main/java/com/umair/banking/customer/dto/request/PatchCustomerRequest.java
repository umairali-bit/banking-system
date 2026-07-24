package com.umair.banking.customer.dto.request;

import com.umair.banking.validation.annotation.ValidName;
import com.umair.banking.validation.annotation.ValidPhone;
import jakarta.validation.constraints.Email;

public record PatchCustomerRequest(

        @ValidName
        String firstName,

        @ValidName
        String lastName,

        @Email
        String email,

        @ValidPhone
        String phoneNumber
) {}
