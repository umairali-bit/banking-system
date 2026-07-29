package com.umair.banking.customer.repository;

import com.umair.banking.customer.entity.Customer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository <Customer, Long> {


    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String s);

    boolean existsByCustomerNumber(String uniqueCustomerNumber);
}
