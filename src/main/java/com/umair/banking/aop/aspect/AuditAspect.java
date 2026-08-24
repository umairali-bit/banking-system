package com.umair.banking.aop.aspect;


import com.umair.banking.aop.annotation.Auditable;
import com.umair.banking.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;

    @AfterReturning(
            pointcut = "@annotation(auditable)"
    )
    public void audit(JoinPoint joinPoint,
                      Auditable auditable) {

        Object[] args = joinPoint.getArgs();

        Long entityId = (Long) args[0];

        auditService.log(
                auditable.action(),
                auditable.entityType(),
                entityId,
                auditable.details()

        );
        log.info(
                "Audit recorded: action={}, entityType={}, entityId={}",
                auditable.action(),
                auditable.entityType(),
                entityId
        );
    }
}
