package com.umair.banking.security.passwordreset.service.impl;


import com.umair.banking.exception.EmailNotFoundException;
import com.umair.banking.notification.dto.EmailNotification;
import com.umair.banking.notification.service.EmailService;
import com.umair.banking.security.entity.User;
import com.umair.banking.security.passwordreset.dto.PasswordResetRequest;
import com.umair.banking.security.passwordreset.entity.PasswordResetToken;
import com.umair.banking.security.passwordreset.repository.PasswordResetTokenRepository;
import com.umair.banking.security.passwordreset.service.PasswordResetService;
import com.umair.banking.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetTokenRepository  passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;


    @Override
    public void forgotPassword(PasswordResetRequest request) {

        User user = userRepository.findByEmail(request.email()).orElseThrow(
                () -> new EmailNotFoundException("Email not found")
        );

        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));


        passwordResetTokenRepository.save(passwordResetToken);

        EmailNotification notification = new EmailNotification(
                user.getEmail(),
                "Password Reset Request",
                "Hello " + user.getUsername()
                        + ",\n\nWe received a request to reset your password."
                        + "\n\nYour password reset token is:"
                        + "\n" + token
                        + "\n\nThis token will expire in 15 minutes."
                        + "\n\nIf you did not request a password reset, you can ignore this email."
        );

        emailService.sendEmail(notification);

    }
}
