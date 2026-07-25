package com.umair.banking.generator;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BusinessNumberGenerator {

    public String generateBusinessNumber() {
        return "BUS-" + UUID.randomUUID()
                .toString()
                .substring(0, 10)
                .toUpperCase();
    }
}
