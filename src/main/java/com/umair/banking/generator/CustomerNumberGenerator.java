package com.umair.banking.generator;


import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomerNumberGenerator {

    public String generateCustomerNumber() {
        return "CUS-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();

    }


}
