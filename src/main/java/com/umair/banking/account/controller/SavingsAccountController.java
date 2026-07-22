package com.umair.banking.account.controller;

import com.umair.banking.account.dto.request.CreateSavingsAccountRequest;
import com.umair.banking.account.dto.response.SavingsAccountResponse;
import com.umair.banking.account.service.SavingsAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/saving-accounts")
@RequiredArgsConstructor
public class SavingsAccountController {

    private final SavingsAccountService savingAccountService;

    @PostMapping
    public ResponseEntity<SavingsAccountResponse> createSavingsAccount(@Valid @RequestBody CreateSavingsAccountRequest request) {

       SavingsAccountResponse savingsAccountResponse = savingAccountService.createSavingsAccount(request);

       return ResponseEntity
               .status(HttpStatus.CREATED)
               .body(savingsAccountResponse);
    }
}
