package com.umair.banking.security.jwt.token;

import com.umair.banking.security.entity.User;

public interface TokenService {

    void saveToken(User user, String refreshToken, TokenType tokenType);

    boolean isTokenValid(String jwt);

    void revokeToken(String jwt);

    void revokeAllUserTokens(Long userId);

}
