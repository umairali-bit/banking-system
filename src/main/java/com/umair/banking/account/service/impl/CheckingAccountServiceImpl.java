package com.umair.banking.account.service.impl;

import com.umair.banking.account.dto.request.CreateCheckingAccountRequest;
import com.umair.banking.account.dto.response.CheckingAccountResponse;
import com.umair.banking.account.entity.CheckingAccount;
import com.umair.banking.account.enums.AccountStatus;
import com.umair.banking.account.enums.AccountType;
import com.umair.banking.account.enums.Currency;
import com.umair.banking.customer.entity.Customer;
import com.umair.banking.customer.repository.CustomerRepository;
import com.umair.banking.exception.AccountNotFoundException;
import com.umair.banking.exception.CustomerNotFoundException;
import com.umair.banking.account.repository.AccountRepository;
import com.umair.banking.account.service.CheckingAccountService;
import com.umair.banking.generator.AccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckingAccountServiceImpl implements CheckingAccountService {

    private final AccountRepository accountRepository;
    private final AccountNumberGenerator accountNumbergenerator;
    private final CustomerRepository customerRepository;


    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','EMPLOYEE')"
    )
    @Override
    public CheckingAccountResponse createCheckingAccount(CreateCheckingAccountRequest request) {

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with id " + request.customerId()));

        CheckingAccount checkingAccount = new CheckingAccount();

        checkingAccount.setCustomer(customer);
        checkingAccount.setAccountNumber(accountNumbergenerator.generateUniqueAccountNumber());
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
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE') " +
                    "or @authorizationService.isAccountOwner(#id, authentication)"
    )
    @Override
    public CheckingAccountResponse getById(Long id) {

        CheckingAccount checkingAccount = accountRepository.findById(id)
                .filter(account -> account instanceof CheckingAccount)
                .map(account -> (CheckingAccount) account)
                .orElseThrow(() ->
                        new AccountNotFoundException("Checking account not found with id " + id));

        return toResponse(checkingAccount);
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','EMPLOYEE')"
    )
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
