package com.umair.banking.security.service;

import com.umair.banking.security.dto.request.RegisterRequest;
import com.umair.banking.security.dto.response.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

}
