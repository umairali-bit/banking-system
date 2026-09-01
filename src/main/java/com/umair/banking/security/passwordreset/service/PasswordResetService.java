package com.umair.banking.security.passwordreset.service;

import com.umair.banking.security.passwordreset.dto.PasswordResetRequest;

public interface PasswordResetService {

    void forgotPassword(PasswordResetRequest request);
}
