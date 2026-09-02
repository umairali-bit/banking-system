package com.umair.banking.security.passwordreset.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(

        @NotBlank
        String token,

        @NotBlank
        String newPassword
) {
}
