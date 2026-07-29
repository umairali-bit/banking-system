package com.umair.banking.generator;

import com.umair.banking.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountNumberGenerator {

    private final AccountRepository accountRepository;

    private String generateAccountNumber() {

        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0,12)
                .toUpperCase();
    }

    public String generateUniqueAccountNumber() {

        String uniqueAccountNumber;

        do {
            uniqueAccountNumber = generateAccountNumber();
        } while (accountRepository.existsByAccountNumber(uniqueAccountNumber));

        return uniqueAccountNumber;
    }
}
