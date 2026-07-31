package com.umair.banking.transaction.repository;

import com.umair.banking.transaction.dto.response.TransactionResponse;
import com.umair.banking.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.sourceAccount.customer.customerNumber = :customerNumber
             or t.destinationAccount.customer.customerNumber = :customerNumber
            """)
    List<Transaction> findTransactionsByCustomerNumber(String customerNumber);

    @Query("""
            SELECT DISTINCT t
            FROM Transaction t
            WHERE t.sourceAccount.customer.customerNumber = :customerNumber
             OR t.destinationAccount.customer.customerNumber = :customerNumber
            """)
    List<Transaction> findByCustomerNumber(@Param("customerNumber")String customerNumber);

    Optional<Transaction> findByTransactionReference(String transactionReference);

    boolean existsByTransactionReference(String uniqueTransactionReference);


    @Query("""
        SELECT t
        FROM Transaction t
        WHERE t.sourceAccount.id = :accountId
           OR t.destinationAccount.id = :accountId
        """)
    List<Transaction> findByAccountId( @Param("accountId")Long accountId);

}
