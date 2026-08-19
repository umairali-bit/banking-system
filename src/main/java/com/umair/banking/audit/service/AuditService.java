package com.umair.banking.audit.service;

import com.umair.banking.audit.enums.AuditAction;

public interface AuditService {

    void log(AuditAction action,
             String entityType,
             Long entityId,
             String details);
}
