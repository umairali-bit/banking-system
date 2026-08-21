package com.umair.banking.account.service.impl;

import com.umair.banking.account.entity.Account;
import com.umair.banking.account.enums.AccountStatus;
import com.umair.banking.account.repository.AccountRepository;
import com.umair.banking.account.service.AccountLifeCycleService;
import com.umair.banking.audit.enums.AuditAction;
import com.umair.banking.audit.service.AuditService;
import com.umair.banking.exception.AccountNotFoundException;
import com.umair.banking.exception.InvalidAccountStateException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountLifeCycleServiceImpl implements AccountLifeCycleService {

    private final AccountRepository accountRepository;
    private final AuditService auditService;

    private Account findAccountById(Long accountId) {

        return accountRepository
                .findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with id " + accountId));

    }


    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @Override
    public void freezeAccount(Long accountId) {

        Account account = findAccountById(accountId);

        if(account.getStatus() ==  AccountStatus.FROZEN) {
            throw new InvalidAccountStateException("Account is already frozen");
        }

        if(account.getStatus() ==  AccountStatus.CLOSED) {
            throw new InvalidAccountStateException("A closed account cannot be frozen");
        }

        account.setStatus(AccountStatus.FROZEN);

        accountRepository.save(account);

        auditService.log(
                AuditAction.ACCOUNT_FROZEN,
                "ACCOUNT",
                account.getId(),
                "Account frozen"
        );



    }
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @Override
    public void activateAccount(Long accountId) {

        Account account = findAccountById(accountId);

        if(account.getStatus() ==  AccountStatus.ACTIVE) {
            throw new InvalidAccountStateException("Account is already active");
        }

        if(account.getStatus() ==  AccountStatus.CLOSED) {
            throw new InvalidAccountStateException("A closed account cannot be activated");
        }

        account.setStatus(AccountStatus.ACTIVE);

        accountRepository.save(account);

        auditService.log(
                AuditAction.ACCOUNT_ACTIVATED,
                "ACCOUNT",
                account.getId(),
                "Account activated"
        );

    }
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @Override
    public void closeAccount(Long accountId) {

        Account account = findAccountById(accountId);

        if(account.getStatus() ==  AccountStatus.CLOSED) {
            throw new InvalidAccountStateException("Account is already closed");
        }

        account.setStatus(AccountStatus.CLOSED);

        accountRepository.save(account);

        auditService.log(
                AuditAction.ACCOUNT_CLOSED,
                "ACCOUNT",
                account.getId(),
                "Account closed"
        );

    }
}
