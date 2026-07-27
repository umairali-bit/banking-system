package com.umair.banking.common.service;

import com.umair.banking.account.repository.AccountRepository;
import com.umair.banking.generator.AccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountNumberService {

    private final AccountRepository accountRepository;
    private final AccountNumberGenerator accountNumberGenerator;

    public String generateUniqueAccountNumber() {

        String uniqueAccountNumber;

        do {
            uniqueAccountNumber = accountNumberGenerator.generateAccountNumber();
        } while (accountRepository.existsByAccountNumber(uniqueAccountNumber));

        return uniqueAccountNumber;
    }
}
