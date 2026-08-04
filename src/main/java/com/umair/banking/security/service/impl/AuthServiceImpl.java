package com.umair.banking.security.service.impl;

import com.umair.banking.security.dto.request.RegisterRequest;
import com.umair.banking.security.dto.response.UserResponse;
import com.umair.banking.security.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {


    @Override
    public UserResponse register(RegisterRequest request) {
        return null;
    }
}
