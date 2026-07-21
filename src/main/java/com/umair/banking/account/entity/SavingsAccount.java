package com.umair.banking.account.entity;

import com.umair.banking.account.enums.AccountStatus;
import com.umair.banking.account.enums.Currency;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity
public class SavingsAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String accountNumber;
    String customerName;
    BigDecimal balance;

    @Enumerated(EnumType.STRING)
    Currency currency;

    @Enumerated(EnumType.STRING)
    AccountStatus status;

    LocalDateTime createdAt;
}
