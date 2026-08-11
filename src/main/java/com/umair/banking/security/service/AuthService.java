package com.umair.banking.security.service;

import com.umair.banking.security.dto.request.LoginRequest;
import com.umair.banking.security.dto.request.RefreshTokenRequest;
import com.umair.banking.security.dto.request.RegisterRequest;
import com.umair.banking.security.dto.response.LoginResponse;
import com.umair.banking.security.dto.response.RefreshTokenResponse;
import com.umair.banking.security.dto.response.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

}
