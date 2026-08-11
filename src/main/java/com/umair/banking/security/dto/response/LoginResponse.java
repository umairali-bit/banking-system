package com.umair.banking.security.dto.response;

import java.util.Set;

public record LoginResponse(
        String username,
        Set<String> roles,
        String accessToken,
        String refreshToken,
        String tokenType,
        String message
) {
}
