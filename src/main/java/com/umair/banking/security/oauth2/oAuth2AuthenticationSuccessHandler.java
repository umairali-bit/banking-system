package com.umair.banking.security.oauth2;


import com.umair.banking.security.entity.User;
import com.umair.banking.security.jwt.JwtService;
import com.umair.banking.security.jwt.token.TokenService;
import com.umair.banking.security.jwt.token.TokenType;
import com.umair.banking.security.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class oAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenService tokenService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        //getting google user
        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();

        //get users email
        String email = oidcUser.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Banking user not found for email: " + email));

        //generate jwts for the user
        String accessToken = jwtService.generateAccessToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        tokenService.saveToken(
                user,
                accessToken,
                TokenType.ACCESS
        );

        tokenService.saveToken(
                user,
                refreshToken,
                TokenType.REFRESH
        );


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(
                """
                {
                    "accessToken": "%s",
                    "refreshToken": "%s",
                    "tokenType": "Bearer"
                }
                """.formatted(
                        accessToken,
                        refreshToken
                )
        );

        response.getWriter().flush();

    }
}
