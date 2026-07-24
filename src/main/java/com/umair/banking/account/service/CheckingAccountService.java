package com.umair.banking.account.service;

import com.umair.banking.account.dto.request.CreateCheckingAccountRequest;
import com.umair.banking.account.dto.response.CheckingAccountResponse;
import com.umair.banking.common.service.BaseService;

public interface CheckingAccountService extends BaseService<CheckingAccountResponse, Long> {

    CheckingAccountResponse createCheckingAccount(CreateCheckingAccountRequest createCheckingAccountRequest);
}
