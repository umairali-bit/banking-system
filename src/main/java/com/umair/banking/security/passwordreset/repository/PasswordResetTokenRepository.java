package com.umair.banking.security.passwordreset.repository;

import com.umair.banking.security.passwordreset.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

}
