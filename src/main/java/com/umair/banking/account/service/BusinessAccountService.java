package com.umair.banking.account.service;

import com.umair.banking.account.dto.request.CreateBusinessAccountRequest;
import com.umair.banking.account.dto.response.BusinessAccountResponse;
import com.umair.banking.account.dto.response.CheckingAccountResponse;
import com.umair.banking.common.service.BaseService;

import java.math.BigDecimal;

public interface BusinessAccountService extends BaseService<BusinessAccountResponse, Long> {

    BusinessAccountResponse createBusinessAccount (CreateBusinessAccountRequest request);

}
