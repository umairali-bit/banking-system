package com.umair.banking.security.dto.response;

public record RefreshTokenResponse(

        String accessToken,
        String refreshToken,
        String tokenType
) {
}
