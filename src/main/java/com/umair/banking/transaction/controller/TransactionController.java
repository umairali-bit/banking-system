package com.umair.banking.transaction.controller;


import com.umair.banking.transaction.dto.request.DepositRequest;
import com.umair.banking.transaction.dto.request.TransferRequest;
import com.umair.banking.transaction.dto.request.WithdrawRequest;
import com.umair.banking.transaction.dto.response.DepositResponse;
import com.umair.banking.transaction.dto.response.TransactionResponse;
import com.umair.banking.transaction.dto.response.TransferResponse;
import com.umair.banking.transaction.dto.response.WithdrawResponse;
import com.umair.banking.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<DepositResponse> deposit(
            @Valid @RequestBody DepositRequest depositRequest) {
        return ResponseEntity.ok(transactionService.deposit(depositRequest));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawResponse> withdraw(
            @Valid @RequestBody WithdrawRequest withdrawRequest) {
        return ResponseEntity.ok(transactionService.withdraw(withdrawRequest));

    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(
            @Valid @RequestBody TransferRequest transferRequest
    ) {
        return ResponseEntity.ok(transactionService.transfer(transferRequest));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponse> getTransaction(
            @Valid @PathVariable("transactionId") Long transactionId) {
        return ResponseEntity.ok(transactionService.getTransactionById(transactionId));

    }

    @GetMapping("/reference/{transactionReference}")
    public ResponseEntity<TransactionResponse> getReference(
            @Valid @PathVariable("transactionReference") String transactionReference) {
        return ResponseEntity.ok(transactionService.getTransactionByReference(transactionReference));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getAccount(
            @Valid @PathVariable("accountId") Long accountId
    ) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
    }

    @GetMapping("/customer/{customerNumber}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByCustomerNumber(
             @PathVariable String customerNumber
    ) {
        return ResponseEntity.ok(transactionService.getTransactionsByCustomerNumber(customerNumber));

    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

}
