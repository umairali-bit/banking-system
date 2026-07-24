package com.umair.banking.account.service.impl;

import com.umair.banking.account.dto.request.CreateCheckingAccountRequest;
import com.umair.banking.account.dto.response.CheckingAccountResponse;
import com.umair.banking.account.entity.CheckingAccount;
import com.umair.banking.account.enums.AccountStatus;
import com.umair.banking.account.enums.AccountType;
import com.umair.banking.account.enums.Currency;
import com.umair.banking.customer.entity.Customer;
import com.umair.banking.customer.repository.CustomerRepository;
import com.umair.banking.generator.AccountNumberGenerator;
import com.umair.banking.account.repository.AccountRepository;
import com.umair.banking.account.service.CheckingAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckingAccountServiceImpl implements CheckingAccountService {

    private final AccountRepository accountRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final CustomerRepository customerRepository;



    @Override
    public CheckingAccountResponse createCheckingAccount(CreateCheckingAccountRequest request) {

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer not found with id " + request.customerId()));

        CheckingAccount checkingAccount = new CheckingAccount();

        checkingAccount.setCustomer(customer);
        checkingAccount.setAccountNumber(accountNumberGenerator.generateAccountNumber());
        checkingAccount.setAccountType(AccountType.CHECKING);
        checkingAccount.setBalance(request.openingBalance());
        checkingAccount.setCurrency(request.currency());
        checkingAccount.setStatus(AccountStatus.ACTIVE);
        checkingAccount.setOverdraftLimit(getDefaultOverdraftLimit(request.currency()));

        customer.getAccounts().add(checkingAccount);

        checkingAccount = accountRepository.save(checkingAccount);

        return toResponse(checkingAccount);
    }

    private BigDecimal getDefaultOverdraftLimit(Currency currency) {
        return switch (currency) {
            case USD -> BigDecimal.valueOf(1000);
            case MYR -> BigDecimal.valueOf(900);
            case PKR -> BigDecimal.valueOf(250000);
        };
    }

    @Override
    public CheckingAccountResponse getById(Long id) {

        CheckingAccount checkingAccount = accountRepository.findById(id)
                .filter(account -> account instanceof CheckingAccount)
                .map(account -> (CheckingAccount) account)
                .orElseThrow(() ->
                        new RuntimeException("Checking account not found with id " + id));

        return toResponse(checkingAccount);
    }

    @Override
    public List<CheckingAccountResponse> getAll() {

        return accountRepository.findAll()
                .stream()
                .filter(account -> account instanceof CheckingAccount)
                .map(account -> (CheckingAccount) account)
                .map(this::toResponse)
                .toList();
    }

    private CheckingAccountResponse toResponse(CheckingAccount account) {

        return new CheckingAccountResponse(
                account.getId(),
                account.getAccountNumber(),

                account.getCustomer().getId(),
                account.getCustomer().getCustomerNumber(),
                account.getCustomer().getFirstName() + " " +
                        account.getCustomer().getLastName(),

                account.getAccountType(),
                account.getBalance(),
                account.getCurrency(),
                account.getOverdraftLimit(),
                account.getStatus(),
                account.getCreatedAt()
        );
    }
}
