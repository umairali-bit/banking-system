package com.umair.banking.security.service.impl;

import com.umair.banking.customer.entity.Customer;
import com.umair.banking.customer.repository.CustomerRepository;
import com.umair.banking.exception.DuplicateEmailException;
import com.umair.banking.exception.DuplicateUserNameException;
import com.umair.banking.security.dto.request.LoginRequest;
import com.umair.banking.security.dto.request.RefreshTokenRequest;
import com.umair.banking.security.dto.request.RegisterRequest;
import com.umair.banking.security.dto.response.LoginResponse;
import com.umair.banking.security.dto.response.LogoutResponse;
import com.umair.banking.security.dto.response.RefreshTokenResponse;
import com.umair.banking.security.dto.response.UserResponse;
import com.umair.banking.security.entity.Role;
import com.umair.banking.security.entity.User;
import com.umair.banking.security.enums.RoleName;
import com.umair.banking.security.jwt.JwtService;
import com.umair.banking.security.jwt.token.TokenService;
import com.umair.banking.security.jwt.token.TokenType;
import com.umair.banking.security.mapper.UserMapper;
import com.umair.banking.security.repository.RoleRepository;
import com.umair.banking.security.repository.UserRepository;
import com.umair.banking.security.service.AuthService;
import com.umair.banking.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenService tokenService;
    private final CustomerRepository customerRepository;


    @Override
    public UserResponse register(RegisterRequest request) {

        validateUser(request);

        Customer customer = getAndValidateCustomer(request);

        Role customerRole = getCustomerRole();

        String encodedPassword =
                passwordEncoder.encode(request.password());

        User user = userMapper.createUser(
                request, encodedPassword, customerRole);

        user.setCustomer(customer);

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

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        tokenService.saveToken(user,
                accessToken,
                TokenType.ACCESS);

        tokenService.saveToken(user,
                refreshToken,
                TokenType.REFRESH);

        Set<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority != null)
                .filter(authority -> authority.startsWith("ROLE_"))
                .map(authority ->
                        authority.replace("ROLE_", ""))
                .collect(Collectors.toSet());


        return new LoginResponse(
                authentication.getName(),
                roles,
                accessToken,
                refreshToken, "Bearer",
                "Login successful"
        );
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {

        String refreshToken = request.refreshToken();

        String tokenType = jwtService.extractTokenType(refreshToken);

        if (!"REFRESH".equals(tokenType)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        Long userId = jwtService.extractUserId(refreshToken);

        User user = customUserDetailsService.getUserById(userId);

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        if (!tokenService.isTokenValid(refreshToken)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");

        }

        tokenService.revokeToken(refreshToken);

        String newAccessToken = jwtService.generateAccessToken(user);

        String newRefreshToken = jwtService.generateRefreshToken(user);

        return new RefreshTokenResponse(
                newAccessToken,
                newRefreshToken,
                "Bearer"
        );
    }

    @Override
    public LogoutResponse logout(String accessToken) {

        Long userId = jwtService.extractUserId(accessToken);
        User user = customUserDetailsService.getUserById(userId);

        if (!jwtService.isTokenValid(accessToken, user)) {
            throw new IllegalArgumentException("Invalid or expired access token");
        }

        if (!tokenService.isTokenValid(accessToken)) {
            throw new IllegalArgumentException("Token already expired or revoked");

        }

        tokenService.revokeAllUserTokens(userId);

        return new LogoutResponse("Logout successful");
    }

    public void validateUser(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException(
                    "Email already exists"
            );

        }

        if(userRepository.existsByUsername(request.username())) {
            throw new DuplicateUserNameException(
                    "User name already exists"
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

    public Customer getAndValidateCustomer(RegisterRequest request) {

        Customer customer = customerRepository.findByCustomerNumber(request.customerNumber())
                .orElseThrow(() -> new IllegalArgumentException("Customer number not found with number "
                        + request.customerNumber()));

        if (!customer.getEmail().equalsIgnoreCase(request.email())) {
            throw new IllegalArgumentException("Registration email does not match customer email");
        }

        if (userRepository.existsByCustomerId(customer.getId())) {
            throw new IllegalArgumentException("Customer already has an online banking account");
        }

        return customer;


    }




}



