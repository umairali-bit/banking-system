package com.umair.banking.account.service;

import com.umair.banking.account.dto.request.CreateSavingsAccountRequest;
import com.umair.banking.account.dto.response.SavingsAccountResponse;

public interface SavingsAccountService {

    SavingsAccountResponse createSavingsAccount(CreateSavingsAccountRequest request);

}
