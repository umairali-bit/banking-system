package com.umair.banking.account.service.impl;

import com.umair.banking.account.entity.SavingsAccount;
import com.umair.banking.account.enums.AccountStatus;
import com.umair.banking.account.enums.AccountType;
import com.umair.banking.customer.entity.Customer;
import com.umair.banking.customer.repository.CustomerRepository;
import com.umair.banking.generator.AccountNumberGenerator;
import com.umair.banking.account.repository.AccountRepository;
import com.umair.banking.account.service.SavingsAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import com.umair.banking.account.dto.request.CreateSavingsAccountRequest;
import com.umair.banking.account.dto.response.SavingsAccountResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SavingsAccountServiceImpl implements SavingsAccountService {

    private final AccountRepository accountRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final CustomerRepository customerRepository;

    private static final BigDecimal DEFAULT_INTEREST_RATE = BigDecimal.valueOf(2.50);

    @Override
    public SavingsAccountResponse createSavingsAccount(CreateSavingsAccountRequest request) {

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer not found with id " + request.customerId()));

        SavingsAccount savingsAccount = new SavingsAccount();

        savingsAccount.setCustomer(customer);
        savingsAccount.setAccountNumber(accountNumberGenerator.generateAccountNumber());
        savingsAccount.setAccountType(AccountType.SAVINGS);
        savingsAccount.setBalance(request.openingBalance());
        savingsAccount.setCurrency(request.currency());
        savingsAccount.setStatus(AccountStatus.ACTIVE);
        savingsAccount.setInterestRate(DEFAULT_INTEREST_RATE);

        customer.getAccounts().add(savingsAccount);

        SavingsAccount savedSavingsAccount = accountRepository.save(savingsAccount);

        return toResponse(savedSavingsAccount);


    }

    @Override
    public SavingsAccountResponse getById(Long id) {

        SavingsAccount savingsAccount = (SavingsAccount) accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        return toResponse(savingsAccount);
    }


    @Override
    public List<SavingsAccountResponse> getAll() {

        return accountRepository.findAll()
                .stream()
                .filter(account -> account instanceof SavingsAccount)
                .map(account -> (SavingsAccount) account)
                .map(i -> this.toResponse(i))
                .toList();
    }

    private SavingsAccountResponse toResponse(SavingsAccount account) {

        return new SavingsAccountResponse(
                account.getId(),
                account.getAccountNumber(),

                account.getCustomer().getId(),
                account.getCustomer().getCustomerNumber(),
                account.getCustomer().getFirstName() + " " +
                        account.getCustomer().getLastName(),

                account.getAccountType(),
                account.getBalance(),
                account.getCurrency(),
                account.getInterestRate(),
                account.getStatus(),
                account.getCreatedAt()
        );

    }
}

