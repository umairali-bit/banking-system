package com.umair.banking.security.jwt.token.Impl;

import com.umair.banking.security.entity.User;
import com.umair.banking.security.jwt.token.Token;
import com.umair.banking.security.jwt.token.TokenRepository;
import com.umair.banking.security.jwt.token.TokenService;
import com.umair.banking.security.jwt.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    @Override
    public void saveToken(User user, String jwt, TokenType tokenType) {

        Token token = new Token();
        token.setToken(jwt);
        token.setTokenType(tokenType);
        token.setRevoked(false);
        token.setExpired(false);
        token.setUser(user);

        tokenRepository.save(token);

    }

    @Override
    public boolean isTokenValid(String jwt) {

        return tokenRepository.findByToken(jwt)
                .map(token -> !token.isRevoked()
                        &&!token.isExpired()
                )
                .orElse(false);
    }

    @Override
    public void revokeToken(String jwt) {

        Token token = tokenRepository.findByToken(jwt)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));

        token.setRevoked(true);
        tokenRepository.save(token);

    }

    @Override
    public void revokeAllUserTokens(Long userId) {

        List<Token> validTokens =
                tokenRepository.findAllByUserIdAndRevokedFalse(userId);

        List<Token> revokedTokens = validTokens.stream()
                .map(token -> {
                    token.setRevoked(true);
                    return token;
                })
                .toList();

        tokenRepository.saveAll(revokedTokens);
    }
}
