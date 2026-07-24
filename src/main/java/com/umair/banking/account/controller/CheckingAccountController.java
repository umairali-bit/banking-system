package com.umair.banking.account.controller;

import com.umair.banking.account.dto.request.CreateCheckingAccountRequest;
import com.umair.banking.account.dto.response.CheckingAccountResponse;
import com.umair.banking.account.service.CheckingAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/checking-accounts")
@RequiredArgsConstructor
public class CheckingAccountController {

    private final CheckingAccountService checkingAccountService;

    @PostMapping
    public ResponseEntity<CheckingAccountResponse> createCheckingAccount(@Valid @RequestBody CreateCheckingAccountRequest request) {

        CheckingAccountResponse checkingAccountResponse = checkingAccountService.createCheckingAccount(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(checkingAccountResponse);
    }
    @GetMapping("/{accountId}")
    public ResponseEntity<CheckingAccountResponse> getCheckingAccountById(
            @PathVariable Long accountId) {

        return ResponseEntity.ok(
                checkingAccountService.getById(accountId)
        );
    }

    @GetMapping
    public ResponseEntity<List<CheckingAccountResponse>> getAllCheckingAccounts() {

        return ResponseEntity.ok(
                checkingAccountService.getAll()
        );
    }
}
