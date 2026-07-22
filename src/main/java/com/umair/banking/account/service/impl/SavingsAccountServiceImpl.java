package com.umair.banking.account.service.impl;

import com.umair.banking.account.entity.SavingsAccount;
import com.umair.banking.account.enums.AccountStatus;
import com.umair.banking.account.enums.AccountType;
import com.umair.banking.account.generator.AccountNumberGenerator;
import com.umair.banking.account.repository.SavingsAccountRepository;
import com.umair.banking.account.service.SavingsAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import com.umair.banking.account.dto.request.CreateSavingsAccountRequest;
import com.umair.banking.account.dto.response.SavingsAccountResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class SavingsAccountServiceImpl implements SavingsAccountService {

    private final SavingsAccountRepository savingsAccountRepository;
    private final AccountNumberGenerator accountNumberGenerator;

    private static final BigDecimal DEFAULT_INTEREST_RATE = BigDecimal.valueOf(2.50);

    @Override
    public SavingsAccountResponse createSavingsAccount(CreateSavingsAccountRequest request) {

        SavingsAccount savingsAccount = new SavingsAccount();

        savingsAccount.setFirstName(request.firstName());
        savingsAccount.setLastName(request.lastName());
        savingsAccount.setAccountNumber(accountNumberGenerator.generateAccountNumber());
        savingsAccount.setAccountType(AccountType.SAVINGS);
        savingsAccount.setBalance(request.openingBalance());
        savingsAccount.setCurrency(request.currency());
        savingsAccount.setStatus(AccountStatus.ACTIVE);
        savingsAccount.setInterestRate(DEFAULT_INTEREST_RATE);
        savingsAccount.setCreatedAt(LocalDateTime.now());

        SavingsAccount savedSavingsAccount = savingsAccountRepository.save(savingsAccount);

        return new SavingsAccountResponse(
                savedSavingsAccount.getId(),
                savedSavingsAccount.getAccountNumber(),
                savedSavingsAccount.getFirstName(),
                savedSavingsAccount.getLastName(),
                savedSavingsAccount.getAccountType(),
                savedSavingsAccount.getBalance(),
                savedSavingsAccount.getCurrency(),
                savedSavingsAccount.getInterestRate(),
                savedSavingsAccount.getStatus(),
                savedSavingsAccount.getCreatedAt()
        );


    }
}

