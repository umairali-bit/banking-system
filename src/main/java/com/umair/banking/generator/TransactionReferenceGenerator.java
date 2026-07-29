package com.umair.banking.generator;


import com.umair.banking.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class TransactionReferenceGenerator {

    private final TransactionRepository transactionRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int REFERENCE_PART_LENGTH = 12;

    private final SecureRandom random = new SecureRandom();

    private String generateRandomString() {

        StringBuilder builder = new StringBuilder("TXN-");

        for(int i = 0; i < REFERENCE_PART_LENGTH; i++) {
            builder.append(
                    CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return builder.toString();


    }

    public String generateUniqueTransactionReference() {

        String uniqueTransactionReference;

        do {
            uniqueTransactionReference = generateRandomString();
        } while (transactionRepository.existsByTransactionReference(uniqueTransactionReference));

        return uniqueTransactionReference;
    }



}
