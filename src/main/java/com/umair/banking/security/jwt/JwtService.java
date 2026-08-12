package com.umair.banking.security.jwt;

import com.umair.banking.security.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.umair.banking.security.entity.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;


    public String generateAccessToken(User user) {

        return generateToken(
                user,
                accessTokenExpiration,
                "ACCESS"
        );
    }


    public String generateRefreshToken(User user) {

        return generateToken(
                user,
                refreshTokenExpiration,
                "REFRESH"
        );
    }


    private String generateToken(
            User user,
            long expiration,
            String tokenType
    ) {

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("token_type", tokenType)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(
                        new Date(
                                System.currentTimeMillis() + expiration
                        )
                )
                .signWith(getSigningKey())
                .compact();
    }


    public Long extractUserId(String token) {

        String subject = extractClaim(
                token,
                Claims::getSubject
        );

        return Long.valueOf(subject);
    }


    public String extractTokenType(String token) {

        return extractClaim(
                token,
                claims ->
                        claims.get(
                                "token_type",
                                String.class
                        )
        );
    }


    public boolean isTokenValid(
            String token,
            User user
    ) {

        Long userId = extractUserId(token);

        return userId.equals(user.getId())
                && !isTokenExpired(token);
    }


    private boolean isTokenExpired(String token) {

        return extractExpiration(token)
                .before(new Date());
    }


    private Date extractExpiration(String token) {

        return extractClaim(
                token,
                Claims::getExpiration
        );
    }


    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {

        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }


    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private SecretKey getSigningKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
