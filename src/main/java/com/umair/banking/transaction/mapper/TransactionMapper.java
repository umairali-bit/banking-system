package com.umair.banking.transaction.mapper;

import com.umair.banking.account.entity.Account;
import com.umair.banking.account.enums.Currency;
import com.umair.banking.generator.TransactionReferenceGenerator;
import com.umair.banking.transaction.dto.response.DepositResponse;
import com.umair.banking.transaction.dto.response.TransactionResponse;
import com.umair.banking.transaction.dto.response.TransferResponse;
import com.umair.banking.transaction.dto.response.WithdrawResponse;
import com.umair.banking.transaction.entity.Transaction;
import com.umair.banking.transaction.enums.TransactionStatus;
import com.umair.banking.transaction.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final TransactionReferenceGenerator transactionReferenceGenerator;

    private Transaction createTransaction(Account sourceAccount,
                                          Account destinationAccount,
                                          BigDecimal sourceAmount,
                                          Currency sourceCurrency,
                                          BigDecimal destinationAmount,
                                          Currency destinationCurrency,
                                          TransactionType transactionType) {

        Transaction transaction = new Transaction();
        transaction.setTransactionReference(transactionReferenceGenerator.generateUniqueTransactionReference());
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        transaction.setSourceAmount(sourceAmount);
        transaction.setSourceCurrency(sourceCurrency);
        transaction.setDestinationAmount(destinationAmount);
        transaction.setDestinationCurrency(destinationCurrency);
        transaction.setTransactionType(transactionType);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);

        return transaction;

    }

    public Transaction toDepositTransaction(Account account, BigDecimal amount) {
        return createTransaction(
                null,
                account,
                amount,
                account.getCurrency(),
                amount,
                account.getCurrency(),
                TransactionType.DEPOSIT
        );


    }

    public Transaction toWithdrawTransaction(Account account, BigDecimal amount) {
        return createTransaction(
                account,
                null,
                amount,
                account.getCurrency(),
                amount,
                account.getCurrency(),
                TransactionType.WITHDRAW


        );
    }

    public Transaction toTransferTransaction(Account sourceAccount,
                                             Account destinationAccount,
                                             BigDecimal sourceAmount,
                                             BigDecimal destinationAmount) {
        return createTransaction(
                sourceAccount,
                destinationAccount,
                sourceAmount,
                sourceAccount.getCurrency(),
                destinationAmount,
                destinationAccount.getCurrency(),
                TransactionType.TRANSFER
        );
    }

    public DepositResponse toDepositResponse(Transaction transaction) {

        return new DepositResponse(
                transaction.getId(),
                transaction.getTransactionReference(),
                transaction.getDestinationAccount().getId(),
                transaction.getDestinationAmount(),
                transaction.getDestinationCurrency(),
                transaction.getDestinationAccount().getBalance(),
                transaction.getTransactionType(),
                transaction.getTransactionStatus(),
                transaction.getCreatedAt()
        );
    }

    public WithdrawResponse toWithdrawResponse(Transaction transaction) {

        return new WithdrawResponse(
                transaction.getId(),
                transaction.getTransactionReference(),
                transaction.getSourceAccount().getId(),
                transaction.getSourceAmount(),
                transaction.getSourceCurrency(),
                transaction.getSourceAccount().getBalance(),
                transaction.getTransactionType(),
                transaction.getTransactionStatus(),
                transaction.getCreatedAt()
        );
    }
    public TransferResponse toTransferResponse(Transaction transaction) {

        return new TransferResponse(
                transaction.getId(),
                transaction.getTransactionReference(),
                transaction.getSourceAccount().getId(),
                transaction.getDestinationAccount().getId(),
                transaction.getSourceAmount(),
                transaction.getSourceCurrency(),
                transaction.getDestinationAmount(),
                transaction.getDestinationCurrency(),
                transaction.getSourceAccount().getBalance(),
                transaction.getDestinationAccount().getBalance(),
                transaction.getTransactionType(),
                transaction.getTransactionStatus(),
                transaction.getCreatedAt()
        );
    }

    public TransactionResponse toTransactionResponse(Transaction transaction) {

        return new TransactionResponse(
                transaction.getId(),
                transaction.getTransactionReference(),

                transaction.getSourceAccount() != null
                        ? transaction.getSourceAccount().getId()
                        : null,

                transaction.getDestinationAccount() != null
                        ? transaction.getDestinationAccount().getId()
                        : null,

                transaction.getSourceAmount(),
                transaction.getSourceCurrency(),
                transaction.getDestinationAmount(),
                transaction.getDestinationCurrency(),
                transaction.getTransactionType(),
                transaction.getTransactionStatus(),
                transaction.getCreatedAt()
        );
    }


}
