package com.umair.banking.audit.config;

import com.umair.banking.security.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<Long> {


    @Override
    public Optional<Long> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {

            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User user)) {
            return Optional.empty();
        }

        return Optional.of(user.getId());
    }

}

