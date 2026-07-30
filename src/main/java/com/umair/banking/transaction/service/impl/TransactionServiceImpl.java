package com.umair.banking.transaction.service.impl;

import com.umair.banking.account.entity.Account;
import com.umair.banking.account.enums.AccountStatus;
import com.umair.banking.account.repository.AccountRepository;
import com.umair.banking.currency.service.CurrencyConversionService;
import com.umair.banking.exception.AccountNotFoundException;
import com.umair.banking.exception.InsufficientFundsExceptions;
import com.umair.banking.exception.InvalidAccountStateException;
import com.umair.banking.transaction.dto.request.DepositRequest;
import com.umair.banking.transaction.dto.request.TransferRequest;
import com.umair.banking.transaction.dto.request.WithdrawRequest;
import com.umair.banking.transaction.dto.response.DepositResponse;
import com.umair.banking.transaction.dto.response.TransactionResponse;
import com.umair.banking.transaction.dto.response.TransferResponse;
import com.umair.banking.transaction.dto.response.WithdrawResponse;
import com.umair.banking.transaction.mapper.TransactionMapper;
import com.umair.banking.transaction.repository.TransactionRepository;
import com.umair.banking.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CurrencyConversionService currencyConversionService;
    private final TransactionMapper transactionMapper;



    private Account findAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(String.format("Account with id %d not found", accountId)
        ));

    }

    private void validateAccountStatus(Account account) {

        if (account.getStatus() == AccountStatus.FROZEN) {
            throw new InvalidAccountStateException(String.format("Account with id %d has been frozen", account.getId()));
        }

        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new InvalidAccountStateException(String.format("Account with id %d has been closed", account.getId()));
        }
    }

    private void insufficientFunds(Account account, BigDecimal amount) {

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsExceptions(
                    "Insufficient balance"
            );
        }
    }


    @Override
    public DepositResponse deposit(DepositRequest depositRequest) {
        return null;
    }

    @Override
    public WithdrawResponse withdraw(WithdrawRequest withdrawRequest) {
        return null;
    }

    @Override
    public TransferResponse transfer(TransferRequest transferRequest) {
        return null;
    }

    @Override
    public TransactionResponse getTransactionById(Long transactionId) {
        return null;
    }

    @Override
    public TransactionResponse getTransactionByReference(String transactionReference) {
        return null;
    }

    @Override
    public List<TransactionResponse> getTransactionsByAccountId(Long accountId) {
        return List.of();
    }

    @Override
    public List<TransactionResponse> getTransactionsByCustomerNumber(String customerNumber) {
        return List.of();
    }

    @Override
    public List<TransactionResponse> getAllTransactions() {
        return List.of();
    }
}
