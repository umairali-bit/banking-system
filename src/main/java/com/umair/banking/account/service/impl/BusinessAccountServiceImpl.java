package com.umair.banking.account.service.impl;

import com.umair.banking.account.dto.request.CreateBusinessAccountRequest;
import com.umair.banking.account.dto.response.BusinessAccountResponse;
import com.umair.banking.account.entity.BusinessAccount;
import com.umair.banking.account.enums.AccountStatus;
import com.umair.banking.account.enums.AccountType;
import com.umair.banking.account.repository.AccountRepository;
import com.umair.banking.account.service.BusinessAccountService;
import com.umair.banking.customer.entity.Customer;
import com.umair.banking.customer.repository.CustomerRepository;
import com.umair.banking.generator.AccountNumberGenerator;
import com.umair.banking.generator.BusinessNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BusinessAccountServiceImpl implements BusinessAccountService {

    private final AccountRepository accountRepository;
    public final BusinessNumberGenerator businessNumberGenerator;
    public final AccountNumberGenerator accountNumberGenerator;
    public final CustomerRepository customerRepository;


    @Override
    public BusinessAccountResponse createBusinessAccount(CreateBusinessAccountRequest request) {

        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + request.customerId()));

        BusinessAccount businessAccount = new BusinessAccount();

        businessAccount.setCustomer(customer);
        businessAccount.setAccountNumber(accountNumberGenerator.generateAccountNumber());
        businessAccount.setRegistrationNumber(businessNumberGenerator.generateBusinessNumber());
        businessAccount.setBusinessName(request.businessName());
        businessAccount.setBalance(request.openingBalance());
        businessAccount.setCurrency(request.currency());
        businessAccount.setAccountType(AccountType.BUSINESS);
        businessAccount.setStatus(AccountStatus.ACTIVE);
        businessAccount.setCreditLimit(determineCreditLimit(request.openingBalance()));

        customer.getAccounts().add(businessAccount);

        businessAccount = accountRepository.save(businessAccount);

        return toResponse(businessAccount);
    }

    @Override
    public BusinessAccountResponse getById(Long id) {

        BusinessAccount businessAccount = accountRepository.findById(id)
                .filter(account -> account instanceof BusinessAccount)
                .map(account -> (BusinessAccount) account )
                .orElseThrow(() -> new RuntimeException("BusinessAccount not found with id: " + id));

        return toResponse(businessAccount);
    }

    @Override
    public List<BusinessAccountResponse> getAll() {

        return accountRepository.findAll()
                .stream()
                .filter(account -> account instanceof BusinessAccount)
                .map(account -> (BusinessAccount) account)
                .map(i -> this.toResponse(i))
                .toList();
    }

    BigDecimal determineCreditLimit(BigDecimal openingBalance) {

        if(openingBalance.compareTo(BigDecimal.valueOf(10_000)) < 0) {
            return BigDecimal.valueOf(25_000);
        }

        if(openingBalance.compareTo(BigDecimal.valueOf(50_000)) < 0) {
            return BigDecimal.valueOf(100_000);
        }

        if(openingBalance.compareTo(BigDecimal.valueOf(250_000)) < 0) {
            return BigDecimal.valueOf(500_000);
        }

        return BigDecimal.valueOf(1_000_000);

    }

    private BusinessAccountResponse toResponse(BusinessAccount account) {

        return new BusinessAccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getBusinessName(),
                account.getRegistrationNumber(),

                account.getCustomer().getId(),
                account.getCustomer().getCustomerNumber(),
                account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName(),

                account.getAccountType(),
                account.getBalance(),
                account.getCurrency(),
                account.getCreditLimit(),
                account.getStatus(),
                account.getCreatedAt()
        );

    }




}
