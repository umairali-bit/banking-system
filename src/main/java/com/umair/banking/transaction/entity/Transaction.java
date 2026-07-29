package com.umair.banking.transaction.entity;


import com.umair.banking.account.entity.Account;
import com.umair.banking.account.enums.Currency;
import com.umair.banking.transaction.enums.TransactionStatus;
import com.umair.banking.transaction.enums.TransactionType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 16, updatable = false)
    private String transactionReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account_id")
    private Account sourceAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal sourceAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency sourceCurrency;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal destinationAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency destinationCurrency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus transactionStatus;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }








}
