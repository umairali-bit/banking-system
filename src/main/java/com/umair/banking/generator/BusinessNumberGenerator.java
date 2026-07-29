package com.umair.banking.generator;

import com.umair.banking.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BusinessNumberGenerator {

    private final AccountRepository accountRepository;

    private String generateBusinessNumber() {
        return "BUS-" + UUID.randomUUID()
                .toString()
                .substring(0, 10)
                .toUpperCase();
    }

    public String generateUniqueBusinessNumber() {

        String uniqueBusinessNumber;

        do {
            uniqueBusinessNumber = generateBusinessNumber();
        } while (accountRepository.existsByAccountNumber(uniqueBusinessNumber));

        return uniqueBusinessNumber;
    }
}
