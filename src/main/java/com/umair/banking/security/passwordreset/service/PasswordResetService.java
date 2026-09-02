package com.umair.banking.security.passwordreset.service;

import com.umair.banking.security.passwordreset.dto.PasswordResetRequest;
import com.umair.banking.security.passwordreset.dto.ResetPasswordRequest;

public interface PasswordResetService {

    void forgotPassword(PasswordResetRequest request);

    void resetPassword(ResetPasswordRequest request);
}
