package com.umair.banking.security.repository;

import com.umair.banking.security.entity.Role;
import com.umair.banking.security.enums.RoleName;
import com.umair.banking.security.service.impl.AuthServiceImpl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleName roleName);
}
