package com.umair.banking.security.controller;

import com.umair.banking.security.dto.request.LoginRequest;
import com.umair.banking.security.dto.request.RefreshTokenRequest;
import com.umair.banking.security.dto.request.RegisterRequest;
import com.umair.banking.security.dto.response.LoginResponse;
import com.umair.banking.security.dto.response.LogoutResponse;
import com.umair.banking.security.dto.response.RefreshTokenResponse;
import com.umair.banking.security.dto.response.UserResponse;
import com.umair.banking.security.passwordreset.dto.PasswordResetRequest;
import com.umair.banking.security.passwordreset.dto.ResetPasswordRequest;
import com.umair.banking.security.passwordreset.service.PasswordResetService;
import com.umair.banking.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

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

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("Authorization") String authHead) {

        String accessToken = authHead.substring(7);

        LogoutResponse logoutResponse = authService.logout(accessToken);

        return ResponseEntity.ok(logoutResponse);


    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String>  forgotPassword(@Valid @RequestBody PasswordResetRequest request) {

        passwordResetService.forgotPassword(request);

        return ResponseEntity.ok("Password reset email has been sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {

        passwordResetService.resetPassword(request);

        return ResponseEntity.ok("Password has been reset successfully");
    }
}
