package com.umair.banking.account.controller;

import com.umair.banking.account.service.AccountLifeCycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountLifeCycleController {

    private final AccountLifeCycleService accountLifeCycleService;

    @PatchMapping("/{accountId}/freeze")
    public ResponseEntity<Void> freeze(@PathVariable Long accountId) {

        accountLifeCycleService.freezeAccount(accountId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{accountId}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long accountId) {
        accountLifeCycleService.activateAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{accountId}/close")
    public ResponseEntity<Void> close(@PathVariable Long accountId) {
        accountLifeCycleService.closeAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}
