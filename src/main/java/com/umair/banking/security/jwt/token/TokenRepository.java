package com.umair.banking.security.jwt.token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {


    Optional<Token> findByToken(String jwt);

    List<Token> findAllByUserIdAndRevokedFalse(Long userId);
}
