package com.umair.banking.transaction.service.impl;

import com.umair.banking.account.entity.Account;
import com.umair.banking.account.enums.AccountStatus;
import com.umair.banking.account.enums.Currency;
import com.umair.banking.account.repository.AccountRepository;
import com.umair.banking.currency.service.CurrencyConversionService;
import com.umair.banking.exception.AccountNotFoundException;
import com.umair.banking.exception.InsufficientFundsExceptions;
import com.umair.banking.exception.InvalidAccountStateException;
import com.umair.banking.exception.TransactionNotFoundException;
import com.umair.banking.transaction.dto.request.DepositRequest;
import com.umair.banking.transaction.dto.request.TransferRequest;
import com.umair.banking.transaction.dto.request.WithdrawRequest;
import com.umair.banking.transaction.dto.response.DepositResponse;
import com.umair.banking.transaction.dto.response.TransactionResponse;
import com.umair.banking.transaction.dto.response.TransferResponse;
import com.umair.banking.transaction.dto.response.WithdrawResponse;
import com.umair.banking.transaction.entity.Transaction;
import com.umair.banking.transaction.mapper.TransactionMapper;
import com.umair.banking.transaction.repository.TransactionRepository;
import com.umair.banking.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CurrencyConversionService currencyConversionService;
    private final TransactionMapper transactionMapper;


    private void validateAccountStatus(Account account) {

        if (account.getStatus() == AccountStatus.FROZEN) {
            throw new InvalidAccountStateException(String.format("Account with id %d has been frozen", account.getId()));
        }

        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new InvalidAccountStateException(String.format("Account with id %d has been closed", account.getId()));
        }
    }

    private void validateSufficientFunds(Account account, BigDecimal amount) {

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsExceptions(
                    "Insufficient balance"
            );
        }
    }

    private Account getActiveAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(
                        String.format("Account with id %d not found", accountId)));
        validateAccountStatus(account);
        return account;
    }

    private Account getActiveAccountForUpdate(Long accountId) {

        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new AccountNotFoundException(
                        String.format(
                                "Account with id %d not found",
                                accountId
                        )
                ));

        validateAccountStatus(account);

        return account;
    }

    private void creditAccount(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
    }

    private void debitAccount(Account account, BigDecimal amount) {
        account.setBalance(account.getBalance().subtract(amount));
    }

    private Account save(Account account) {
        return accountRepository.save(account);

    }

    private Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    private BigDecimal convertAmount(BigDecimal amount,
                                     Currency from,
                                     Currency to) {
        return currencyConversionService.convert(amount, from, to);
    }




    @Override
    @Transactional
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE') " +
                    "or @authorizationService.isAccountOwner(#depositRequest.accountId(), authentication)"
    )
    public DepositResponse deposit(DepositRequest depositRequest) {

        Account account = getActiveAccount(depositRequest.accountId());;

        creditAccount(account, depositRequest.amount());

        save(account);

        Transaction transaction = transactionMapper.toDepositTransaction(
                account,
                depositRequest.amount()

        );

        transaction = save(transaction);

        return transactionMapper.toDepositResponse(transaction);
    }

    @Override
    @Transactional
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE') " +
                    "or @authorizationService.isAccountOwner(#withdrawRequest.accountId(), authentication)"
    )
    public WithdrawResponse withdraw(WithdrawRequest withdrawRequest) {

        Account account =
                getActiveAccountForUpdate(withdrawRequest.accountId());

        validateSufficientFunds(account, withdrawRequest.amount());

//        System.out.println(
//                Thread.currentThread().getName()
//                        + " acquired lock on account "
//                        + account.getId()
//        );
//
//        try {
//            Thread.sleep(10_000);
//        } catch (InterruptedException exception) {
//            Thread.currentThread().interrupt();
//
//            throw new IllegalStateException(
//                    "Withdrawal demonstration was interrupted",
//                    exception
//            );
//        }

        debitAccount(account, withdrawRequest.amount());

        save(account);

        Transaction transaction = transactionMapper.toWithdrawTransaction(
                account,
                withdrawRequest.amount()
        );
        transaction = save(transaction);

        return transactionMapper.toWithdrawResponse(transaction);
    }

    @Override
    @Transactional
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE') " +
                    "or @authorizationService.isAccountOwner(" +
                    "#transferRequest.sourceAccountId(), authentication)"
    )
    public TransferResponse transfer(TransferRequest transferRequest) {

        Account sourceAccount = getActiveAccount(
                transferRequest.sourceAccountId());

        Account destinationAccount = getActiveAccount(
                transferRequest.destinationAccountId());

        validateSufficientFunds(sourceAccount, transferRequest.amount());

        BigDecimal destinationAmount;

        if(sourceAccount.getCurrency() == destinationAccount.getCurrency()) {
            destinationAmount = transferRequest.amount();
        } else {
            destinationAmount = convertAmount(transferRequest.amount(),
                    sourceAccount.getCurrency(),
                    destinationAccount.getCurrency());


        }

        debitAccount(sourceAccount, transferRequest.amount());
        creditAccount(destinationAccount, destinationAmount);

        save(sourceAccount);
        save(destinationAccount);

        Transaction transaction = transactionMapper.toTransferTransaction(
                sourceAccount,
                destinationAccount,
                transferRequest.amount(),
                destinationAmount
        );

        transaction = save(transaction);


        return transactionMapper.toTransferResponse(transaction);
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE') " +
                    "or @authorizationService.isAccountOwner(#transactionId, authentication)"
    )
    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long transactionId) {

        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(
                () -> new TransactionNotFoundException(String.format("Transaction with id %d not found", transactionId))
        );

        return transactionMapper.toTransactionResponse(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public TransactionResponse getTransactionByReference(String transactionReference) {

        Transaction transaction = transactionRepository.findByTransactionReference(transactionReference)
                .orElseThrow(() -> new TransactionNotFoundException(String.format("Transaction with reference %s not found", transactionReference))
        );

        return transactionMapper.toTransactionResponse(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE') " +
                    "or @authorizationService.isAccountOwner(#accountId, authentication)"
    )
    public List<TransactionResponse> getTransactionsByAccountId(Long accountId) {

        getActiveAccount(accountId);

        return transactionRepository.findByAccountId(accountId)
                .stream()
                .map(i -> transactionMapper.toTransactionResponse(i))
                .toList();

    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')"
    )
    public List<TransactionResponse> getTransactionsByCustomerNumber(String customerNumber) {

        return transactionRepository.findByCustomerNumber(customerNumber)
                .stream()
                .map(i -> transactionMapper.toTransactionResponse(i))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(i -> transactionMapper.toTransactionResponse(i))
                .toList();
    }
}
