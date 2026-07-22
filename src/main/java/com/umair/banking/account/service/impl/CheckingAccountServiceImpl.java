package com.umair.banking.account.service.impl;

import com.umair.banking.account.dto.request.CreateCheckingAccountRequest;
import com.umair.banking.account.dto.response.CheckingAccountResponse;
import com.umair.banking.account.entity.CheckingAccount;
import com.umair.banking.account.enums.AccountStatus;
import com.umair.banking.account.enums.AccountType;
import com.umair.banking.account.enums.Currency;
import com.umair.banking.account.generator.AccountNumberGenerator;
import com.umair.banking.account.repository.CheckingAccountRepository;
import com.umair.banking.account.service.CheckingAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckingAccountServiceImpl implements CheckingAccountService {

    private final CheckingAccountRepository checkingAccountRepository;
    private final AccountNumberGenerator accountNumberGenerator;



    @Override
    public CheckingAccountResponse createCheckingAccount(CreateCheckingAccountRequest request) {

        CheckingAccount checkingAccount = new CheckingAccount();

        checkingAccount.setFirstName(request.firstName());
        checkingAccount.setLastName(request.lastName());
        checkingAccount.setAccountNumber(accountNumberGenerator.generateAccountNumber());
        checkingAccount.setAccountType(AccountType.CHECKING);
        checkingAccount.setBalance(request.openingBalance());
        checkingAccount.setCurrency(request.currency());
        checkingAccount.setStatus(AccountStatus.ACTIVE);
        checkingAccount.setOverdraftLimit(getDefaultOverdraftLimit(request.currency()));
        checkingAccount.setCreatedAt(LocalDateTime.now());

        CheckingAccount savedCheckingAccount = checkingAccountRepository.save(checkingAccount);

        return new CheckingAccountResponse(
                savedCheckingAccount.getId(),
                savedCheckingAccount.getAccountNumber(),
                savedCheckingAccount.getFirstName(),
                savedCheckingAccount.getLastName(),
                savedCheckingAccount.getAccountType(),
                savedCheckingAccount.getBalance(),
                savedCheckingAccount.getCurrency(),
                savedCheckingAccount.getOverdraftLimit(),
                savedCheckingAccount.getStatus(),
                savedCheckingAccount.getCreatedAt()
        );


    }

    private BigDecimal getDefaultOverdraftLimit(Currency currency) {
        return switch (currency) {
            case USD -> BigDecimal.valueOf(1000);
            case MYR -> BigDecimal.valueOf(900);
            case PKR -> BigDecimal.valueOf(250000);
        };
    }
}
