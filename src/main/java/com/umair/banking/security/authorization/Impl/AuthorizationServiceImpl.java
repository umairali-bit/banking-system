package com.umair.banking.security.authorization.Impl;

import com.umair.banking.account.entity.Account;
import com.umair.banking.account.repository.AccountRepository;
import com.umair.banking.exception.AccountNotFoundException;
import com.umair.banking.security.authorization.AuthorizationService;
import com.umair.banking.security.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service("authorizationService")
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

    private final AccountRepository accountRepository;


    @Override
    public boolean isCustomerOwner(Long customerId, Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        if(user.getCustomer() == null) {
            return false;
        }
        return user.getCustomer().getId().equals(customerId);
    }

    @Override
    public boolean isAccountOwner(Long accountId, Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        if(user.getCustomer() == null) {
            return false;
        }

        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new AccountNotFoundException("Account with id: " + accountId + " not found") );

        return account.getCustomer().getId().equals(user.getCustomer().getId());
    }
}
