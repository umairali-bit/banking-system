package com.umair.banking.account.service;

import com.umair.banking.account.dto.request.CreateSavingsAccountRequest;
import com.umair.banking.account.dto.response.SavingsAccountResponse;
import com.umair.banking.common.service.BaseService;

public interface SavingsAccountService extends BaseService<SavingsAccountResponse, Long> {

    SavingsAccountResponse createSavingsAccount(CreateSavingsAccountRequest request);

}
