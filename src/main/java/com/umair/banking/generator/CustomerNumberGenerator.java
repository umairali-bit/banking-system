package com.umair.banking.generator;


import com.umair.banking.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerNumberGenerator {

    private final CustomerRepository customerRepository;

    private String generateCustomerNumber() {
        return "CUS-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();

    }

    public String generateUniqueCustomerNumber() {

        String uniqueCustomerNumber;

        do {
            uniqueCustomerNumber = generateCustomerNumber();
        } while (customerRepository.existsByCustomerNumber(uniqueCustomerNumber));

        return uniqueCustomerNumber;
    }




}
