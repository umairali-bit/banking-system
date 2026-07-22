package com.umair.banking.account.controller;

import com.umair.banking.account.dto.request.CreateCheckingAccountRequest;
import com.umair.banking.account.dto.response.CheckingAccountResponse;
import com.umair.banking.account.repository.CheckingAccountRepository;
import com.umair.banking.account.service.CheckingAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
