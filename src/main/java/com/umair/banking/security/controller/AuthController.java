package com.umair.banking.security.controller;

import com.umair.banking.security.dto.request.LoginRequest;
import com.umair.banking.security.dto.request.RefreshTokenRequest;
import com.umair.banking.security.dto.request.RegisterRequest;
import com.umair.banking.security.dto.response.LoginResponse;
import com.umair.banking.security.dto.response.RefreshTokenResponse;
import com.umair.banking.security.dto.response.UserResponse;
import com.umair.banking.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {

        UserResponse userResponse = authService.register(registerRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        LoginResponse loginResponse = authService.login(loginRequest);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));


    }
}
