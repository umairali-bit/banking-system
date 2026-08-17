package com.umair.banking.security.mapper;


import com.umair.banking.security.dto.request.RegisterRequest;
import com.umair.banking.security.dto.response.UserResponse;
import com.umair.banking.security.entity.Role;
import com.umair.banking.security.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public User createUser(RegisterRequest request,
                           String encodedPassword,
                           Role customerRole) {
        User user = new User();

        user.setUsername(request.username().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPassword(encodedPassword);

        user.setEnabled(true);
        user.setAccountLocked(false);

        Set<Role> roles = new HashSet<>();
        roles.add(customerRole);

        user.setRoles(roles);

        return user;

    }

    public UserResponse toResponse(User user) {

        Set<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getRoleName().name())
                .collect(Collectors.toSet());

        return  new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles
        );

    }
}
