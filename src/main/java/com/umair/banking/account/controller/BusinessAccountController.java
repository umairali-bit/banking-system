package com.umair.banking.account.controller;

import com.umair.banking.account.dto.request.CreateBusinessAccountRequest;
import com.umair.banking.account.dto.response.BusinessAccountResponse;
import com.umair.banking.account.service.BusinessAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/vi/business-accounts")
@RequiredArgsConstructor
public class BusinessAccountController {

    private final BusinessAccountService businessAccountService;

    @PostMapping
    public ResponseEntity<BusinessAccountResponse> addBusinessAccount(
            @Valid
            @RequestBody CreateBusinessAccountRequest request) {

        BusinessAccountResponse businessAccountResponse = businessAccountService.createBusinessAccount(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(businessAccountResponse);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<BusinessAccountResponse> getBusinessAccountById(
            @PathVariable Long accountId) {
        return ResponseEntity.ok(
                businessAccountService.getById(accountId)
        );
    }

    @GetMapping
    public ResponseEntity<List<BusinessAccountResponse>> getAllBusinessAccounts() {

        return ResponseEntity.ok(
                businessAccountService.getAll()
        );
    }
}
