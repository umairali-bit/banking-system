package com.umair.banking.security.authorization;

import org.springframework.security.core.Authentication;

public interface AuthorizationService {

    boolean isCustomerOwner(Long customerId, Authentication authentication);
}
