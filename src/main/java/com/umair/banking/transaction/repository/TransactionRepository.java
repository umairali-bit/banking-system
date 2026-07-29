package com.umair.banking.transaction.repository;

import com.umair.banking.transaction.dto.response.TransactionResponse;
import com.umair.banking.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
        SELECT t
        FROM Transaction t
        WHERE t.sourceAccount.customer.customerNumber = :customerNumber
         or t.destinationAccount.customer.customerNumber = :customerNumber
        """)
    List<Transaction> findTransactionsByCustomerNumber(String customerNumber);



    boolean existsByTransactionReference(String uniqueTransactionReference);
}
