package com.umair.banking.account.service;

import com.umair.banking.account.dto.request.CreateCheckingAccountRequest;
import com.umair.banking.account.dto.response.CheckingAccountResponse;
import com.umair.banking.account.enums.Currency;
import com.umair.banking.common.service.BaseService;

import java.math.BigDecimal;

public interface CheckingAccountService extends BaseService<CheckingAccountResponse, Long> {

    CheckingAccountResponse createCheckingAccount(CreateCheckingAccountRequest request);

}
