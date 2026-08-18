package com.umair.banking.customer.service.impl;


import com.umair.banking.customer.dto.request.CustomerRequest;
import com.umair.banking.customer.dto.request.PatchCustomerRequest;
import com.umair.banking.customer.dto.response.CustomerResponse;
import com.umair.banking.customer.entity.Customer;
import com.umair.banking.customer.repository.CustomerRepository;
import com.umair.banking.customer.service.CustomerService;
import com.umair.banking.exception.CustomerNotFoundException;
import com.umair.banking.exception.DuplicateEmailException;
import com.umair.banking.exception.DuplicatePhoneNumberException;
import com.umair.banking.generator.CustomerNumberGenerator;
import com.umair.banking.notification.dto.EmailNotification;
import com.umair.banking.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final CustomerNumberGenerator customerNumberGenerator;

    private final EmailService emailService;

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
                orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));
        }

    private void validateEmail(String email) {
        if (customerRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("Customer with email " + email + " already exists");
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        if (customerRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DuplicatePhoneNumberException("Customer with phone number " + phoneNumber + " already exists");
        }
    }



    @PreAuthorize(
            "hasAnyRole('ADMIN','MANAGER','EMPLOYEE')"
    )
    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {

        validateEmail(request.email());

        validatePhoneNumber(request.phoneNumber());

        Customer customer = toEntity(request);

        customer.setCustomerNumber(customerNumberGenerator.generateUniqueCustomerNumber());

        customer = customerRepository.save(customer);

        EmailNotification notification = new EmailNotification(
                customer.getEmail(),
                "Welcome to Online Banking",
                "Hello " + customer.getFirstName()
                        + ", \n\nYour customer number is: "
                        + customer.getCustomerNumber()
                        + " \n\nUse this customer number to register for online banking."

        );

        emailService.sendEmail(notification);

        return toResponse(customer);
    }

    @PreAuthorize(
            "hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')"
    )
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

    @PreAuthorize(
            "hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')"
    )
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

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteCustomer(Long id) {

        Customer customer = findCustomerById(id);

        customerRepository.delete(customer);

    }

    @PreAuthorize(
            "hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE') " +
                    "or @authorizationService.isCustomerOwner(#id, authentication)"
    )
    @Override
    public CustomerResponse getById(Long id) {

        Customer customer = findCustomerById(id);

        return toResponse(customer);
    }
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')"
    )
    @Override
    public List<CustomerResponse> getAll() {
        return customerRepository.findAll()
                .stream()
                .map( i -> this.toResponse(i) )
                .toList();
    }
}
