package com.umair.banking.security.service.impl;

import com.umair.banking.exception.RoleNotFoundException;
import com.umair.banking.exception.UserAlreadyExistsException;
import com.umair.banking.security.dto.request.LoginRequest;
import com.umair.banking.security.dto.request.RegisterRequest;
import com.umair.banking.security.dto.response.LoginResponse;
import com.umair.banking.security.dto.response.UserResponse;
import com.umair.banking.security.entity.Role;
import com.umair.banking.security.entity.User;
import com.umair.banking.security.enums.RoleName;
import com.umair.banking.security.mapper.UserMapper;
import com.umair.banking.security.repository.RoleRepository;
import com.umair.banking.security.repository.UserRepository;
import com.umair.banking.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;


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

    @Override
    public LoginResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        Set<String> roles =  authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .filter(authority-> authority.startsWith("ROLE_"))
                .map(authority ->
                        authority.replace("ROLE_", " "))
                .collect(Collectors.toSet());


        return new LoginResponse(
                authentication.getName(),
                roles,
                "Login successful"
        );
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



