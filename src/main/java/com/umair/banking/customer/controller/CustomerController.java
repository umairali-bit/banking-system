package com.umair.banking.customer.controller;

import com.umair.banking.customer.dto.request.CustomerRequest;
import com.umair.banking.customer.dto.request.PatchCustomerRequest;
import com.umair.banking.customer.dto.response.CustomerResponse;
import com.umair.banking.customer.entity.Customer;
import com.umair.banking.customer.service.CustomerService;
import com.umair.banking.generator.CustomerNumberGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest customerRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerService.createCustomer(customerRequest));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerRequest customerRequest
    ) {
        return ResponseEntity.ok(
                customerService.updateCustomer(customerId, customerRequest)
        );
    }

    @PatchMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> patchCustomer(
            @PathVariable Long customerId,
            @RequestBody PatchCustomerRequest request) {

        return ResponseEntity.ok(
                customerService.patch(customerId, request)
        );
    }

    @DeleteMapping
    public ResponseEntity<CustomerResponse> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long customerId) {

        return ResponseEntity.ok(customerService.getById(customerId));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAll());
    }
}
