package com.umair.banking.audit.repository;

import com.umair.banking.audit.entity.AuditLog;
import com.umair.banking.audit.enums.AuditAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<AuditLog, Long> {

}
