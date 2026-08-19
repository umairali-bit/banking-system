package com.umair.banking.audit.service.impl;

import com.umair.banking.audit.entity.AuditLog;
import com.umair.banking.audit.enums.AuditAction;
import com.umair.banking.audit.repository.AuditRepository;
import com.umair.banking.audit.service.AuditService;
import com.umair.banking.security.entity.User;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    @Override
    public void log(AuditAction action, String entityType, Long entityId, String details) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = null;
        if(authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof User user) {
            userId = user.getId();

        }

        AuditLog auditLog = new AuditLog();
        auditLog.setUserId(userId);
        auditLog.setAction(action);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setDetails(details);



        auditRepository.save(auditLog);

    }
}
