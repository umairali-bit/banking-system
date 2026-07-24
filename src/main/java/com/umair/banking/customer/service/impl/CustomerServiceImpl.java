package com.umair.banking.customer.service.impl;


import com.umair.banking.customer.dto.request.CustomerRequest;
import com.umair.banking.customer.dto.request.PatchCustomerRequest;
import com.umair.banking.customer.dto.response.CustomerResponse;
import com.umair.banking.customer.entity.Customer;
import com.umair.banking.customer.repository.CustomerRepository;
import com.umair.banking.customer.service.CustomerService;
import com.umair.banking.generator.CustomerNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerNumberGenerator customerNumberGenerator;

    private CustomerResponse toResponse(Customer customer) {

        return new  CustomerResponse(
                customer.getId(),
                customer.getCustomerNumber(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getCreatedAt()
        );


    }

    private Customer toEntity(CustomerRequest request) {

        Customer customer = new Customer();

        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setPhoneNumber(request.phoneNumber());

        return customer;
    }


    private Customer findCustomerById(Long id) {

        return customerRepository
                .findById(id).
                orElseThrow(() -> new RuntimeException("Customer with id " + id + " not found"));
        }




    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {

        if (customerRepository.existsByEmail(request.email())) {
        throw new RuntimeException("Email already exists.");
    }

        if (customerRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new RuntimeException("Phone number already exists.");
        }

        Customer customer = toEntity(request);

        customer.setCustomerNumber(customerNumberGenerator.generateCustomerNumber());

        customer = customerRepository.save(customer);

        return toResponse(customer);
    }

    @Override
    public CustomerResponse updateCustomer(Long customerId, CustomerRequest request) {

        Customer customer = findCustomerById(customerId);

        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setPhoneNumber(request.phoneNumber());

        customer = customerRepository.save(customer);

        return toResponse(customer);
    }

    @Override
    public CustomerResponse patch(Long customerId, PatchCustomerRequest request) {

        Customer customer = findCustomerById(customerId);

        if(request.firstName() != null && !request.firstName().isBlank()) {
            customer.setFirstName(request.firstName());
        }

        if(request.lastName() != null && !request.lastName().isBlank()) {
            customer.setLastName(request.lastName());
        }

        if(request.email() != null && !request.email().isBlank()) {
            customer.setEmail(request.email());
        }

        if(request.phoneNumber() != null && !request.phoneNumber().isBlank()) {
            customer.setPhoneNumber(request.phoneNumber());
        }

        customer = customerRepository.save(customer);

        return  toResponse(customer);

    }

    @Override
    public void deleteCustomer(Long id) {

        Customer customer = findCustomerById(id);

        customerRepository.delete(customer);

    }

    @Override
    public CustomerResponse getById(Long id) {

        Customer customer = findCustomerById(id);

        return toResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAll() {
        return customerRepository.findAll()
                .stream()
                .map( i -> this.toResponse(i) )
                .toList();
    }
}
