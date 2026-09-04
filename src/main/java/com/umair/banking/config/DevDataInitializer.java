package com.umair.banking.config;

import com.umair.banking.security.entity.Role;
import com.umair.banking.security.entity.User;
import com.umair.banking.security.enums.RoleName;
import com.umair.banking.security.repository.RoleRepository;
import com.umair.banking.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DevDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) {

        if(userRepository.findByUsername("admin").isPresent()) {
            return;
        }

        Role adminRole = roleRepository.findByRoleName(RoleName.ADMIN)
                .orElseThrow(() -> new RuntimeException("Admin role Not Found"));

        User admin = new User();

        admin.setUsername("admin");
        admin.setEmail("admin@blank.com");
        admin.setPassword(passwordEncoder.encode("Admin123!"));
        admin.setEnabled(true);
        admin.setAccountLocked(false);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setRoles(Set.of(adminRole));

        userRepository.save(admin);




    }
}
