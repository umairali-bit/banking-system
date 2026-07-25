package com.umair.banking.account.entity;


import com.umair.banking.account.enums.AccountStatus;
import com.umair.banking.account.enums.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "business_accounts")
public class BusinessAccount extends Account {


    @Column(nullable = false)
    public String businessName;

    @Column(nullable = false)
    private String registrationNumber;

    @Column(nullable = false)
    private BigDecimal creditLimit;
}
