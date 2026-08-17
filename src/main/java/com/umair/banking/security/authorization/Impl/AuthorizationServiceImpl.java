package com.umair.banking.security.authorization.Impl;

import com.umair.banking.security.authorization.AuthorizationService;
import com.umair.banking.security.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service("authorizationService")
public class AuthorizationServiceImpl implements AuthorizationService {


    @Override
    public boolean isCustomerOwner(Long customerId, Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        if(user.getCustomer() == null) {
            return false;
        }
        return user.getCustomer().getId().equals(customerId);
    }
}
