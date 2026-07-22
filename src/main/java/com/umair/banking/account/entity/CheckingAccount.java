package com.umair.banking.account.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "checking_accounts")
public class CheckingAccount extends Account {

    private BigDecimal overdraftLimit;
}
