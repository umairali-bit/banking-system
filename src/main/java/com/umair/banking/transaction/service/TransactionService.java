package com.umair.banking.transaction.service;

import com.umair.banking.transaction.dto.request.DepositRequest;
import com.umair.banking.transaction.dto.request.TransferRequest;
import com.umair.banking.transaction.dto.request.WithdrawRequest;
import com.umair.banking.transaction.dto.response.DepositResponse;
import com.umair.banking.transaction.dto.response.TransactionResponse;
import com.umair.banking.transaction.dto.response.TransferResponse;
import com.umair.banking.transaction.dto.response.WithdrawResponse;
import com.umair.banking.transaction.entity.Transaction;

import java.util.List;

public interface TransactionService {

    DepositResponse deposit(DepositRequest depositRequest);

    WithdrawResponse withdraw(WithdrawRequest withdrawRequest);

    TransferResponse transfer(TransferRequest transferRequest);

    TransactionResponse getTransactionById(Long transactionId);

    TransactionResponse getTransactionByReference(String transactionReference);

    List<TransactionResponse> getTransactionsByAccountId(Long accountId);

    List<TransactionResponse> getTransactionsByCustomerNumber(String customerNumber);

    List<TransactionResponse> getAllTransactions();
}
