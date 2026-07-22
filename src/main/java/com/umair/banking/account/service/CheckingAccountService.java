package com.umair.banking.account.service;

import com.umair.banking.account.dto.request.CreateCheckingAccountRequest;
import com.umair.banking.account.dto.response.CheckingAccountResponse;

public interface CheckingAccountService {

    CheckingAccountResponse createCheckingAccount(CreateCheckingAccountRequest createCheckingAccountRequest);
}
