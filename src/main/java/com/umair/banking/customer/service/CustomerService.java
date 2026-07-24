package com.umair.banking.customer.service;

import com.umair.banking.common.service.BaseService;
import com.umair.banking.customer.dto.request.CustomerRequest;
import com.umair.banking.customer.dto.request.PatchCustomerRequest;
import com.umair.banking.customer.dto.response.CustomerResponse;

public interface CustomerService extends BaseService<CustomerResponse, Long> {

    CustomerResponse createCustomer(CustomerRequest customerRequest);


    CustomerResponse updateCustomer(Long customerId,
                            CustomerRequest request);

    CustomerResponse patch(Long customerId,
                           PatchCustomerRequest request);

    void deleteCustomer(Long aLong);


}
