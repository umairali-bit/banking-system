package com.umair.banking.security.service.impl;

import com.umair.banking.exception.RoleNotFoundException;
import com.umair.banking.exception.UserAlreadyExistsException;
import com.umair.banking.security.dto.request.RegisterRequest;
import com.umair.banking.security.dto.response.UserResponse;
import com.umair.banking.security.entity.Role;
import com.umair.banking.security.entity.User;
import com.umair.banking.security.enums.RoleName;
import com.umair.banking.security.mapper.UserMapper;
import com.umair.banking.security.repository.RoleRepository;
import com.umair.banking.security.repository.UserRepository;
import com.umair.banking.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    @Override
    public UserResponse register(RegisterRequest request) {

        validateUser(request);

        Role customerRole = getCustomerRole();

        String encodedPassword =
                passwordEncoder.encode(request.password());

        User user = userMapper.createUser(
                request, encodedPassword, customerRole);

        User savedUser = userRepository.save(user);

        return userMapper.toResponse(savedUser);
    }

    public void validateUser(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException(
                    "User already exists"
            );
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException(
                    "Email already exists"
            );
        }
    }

    private Role getCustomerRole() {

        return roleRepository.findByRoleName(RoleName.CUSTOMER)
                .orElseGet(() -> {

                    Role role = new Role();
                    role.setRoleName(RoleName.CUSTOMER);

                    return roleRepository.save(role);
                });


    }


}



