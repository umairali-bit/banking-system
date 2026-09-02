package com.umair.banking.security.passwordreset.service.impl;


import com.umair.banking.exception.EmailNotFoundException;
import com.umair.banking.exception.TokenExpiredException;
import com.umair.banking.exception.TokenNotFoundException;
import com.umair.banking.notification.dto.EmailNotification;
import com.umair.banking.notification.service.EmailService;
import com.umair.banking.security.entity.User;
import com.umair.banking.security.passwordreset.dto.PasswordResetRequest;
import com.umair.banking.security.passwordreset.dto.ResetPasswordRequest;
import com.umair.banking.security.passwordreset.entity.PasswordResetToken;
import com.umair.banking.security.passwordreset.repository.PasswordResetTokenRepository;
import com.umair.banking.security.passwordreset.service.PasswordResetService;
import com.umair.banking.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordResetTokenRepository  passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;


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

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {

        PasswordResetToken token = passwordResetTokenRepository.findByToken(request.token()).orElseThrow(
                () -> new TokenNotFoundException("Invalid password reset token")
        );

        if(token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Password reset token has been expired");
        }

        if(token.isUsed()) {
            throw new IllegalArgumentException("Password reset token has already been used");
        }

        User user = token.getUser();
        String encodedPassword = passwordEncoder.encode(request.newPassword());

        user.setPassword(encodedPassword);

        userRepository.save(user);

        token.setUsed(true);

        passwordResetTokenRepository.save(token);


    }
}
