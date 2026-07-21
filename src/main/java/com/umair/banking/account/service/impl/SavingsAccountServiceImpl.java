package com.umair.banking.account.service.impl;

import com.umair.banking.account.entity.SavingsAccount;
import com.umair.banking.account.enums.AccountStatus;
import com.umair.banking.account.repository.SavingsAccountRepository;
import com.umair.banking.account.service.SavingAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import com.umair.banking.account.dto.request.CreateSavingsAccountRequest;
import com.umair.banking.account.dto.response.SavingsAccountResponse;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SavingsAccountServiceImpl implements SavingAccountService {

    private final SavingsAccountRepository savingsAccountRepository;

    @Override
    public SavingsAccountResponse createSavingsAccount(CreateSavingsAccountRequest request) {

        SavingsAccount savingsAccount = new SavingsAccount();

        savingsAccount.setCustomerName(request.firstName() + " " + request.lastName());
        savingsAccount.setAccountNumber(generateAccountNumber());
        savingsAccount.setBalance(request.openingBalance());
        savingsAccount.setCurrency(request.currency());
        savingsAccount.setStatus(AccountStatus.ACTIVE);
        savingsAccount.setCreatedAt(LocalDateTime.now());

        SavingsAccount savedSavingsAccount = savingsAccountRepository.save(savingsAccount);

        return new SavingsAccountResponse(
                savedSavingsAccount.getId(),
                savedSavingsAccount.getAccountNumber(),
                savedSavingsAccount.getCustomerName(),
                savedSavingsAccount.getBalance(),
                savedSavingsAccount.getCurrency(),
                savedSavingsAccount.getStatus(),
                savedSavingsAccount.getCreatedAt()

        );


    }

    @Override
    public String generateAccountNumber() {

        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0,12)
                .toUpperCase();
    }
}
