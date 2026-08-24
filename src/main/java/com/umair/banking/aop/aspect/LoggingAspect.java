package com.umair.banking.aop.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.umair.banking..service..*(..))")
    public void serviceMethods() {
    }

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info(
                "Execution method: {}",
                joinPoint.getSignature().toShortString()
        );
    }

    @AfterReturning(
            pointcut = "serviceMethods()",
            returning = "result"
    )
    public void logAfterReturning(JoinPoint joinPoint,
                                  Object result) {
        log.info(
                "Method completed successfully: {}",
                joinPoint.getSignature().toShortString()
        );
    }

    @AfterThrowing(
            pointcut = "serviceMethods()",
            throwing = "exception"
    )
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        log.info(
                "Exception in method: {} - {}",
                joinPoint.getSignature().toShortString(),
                exception.getMessage()
        );
    }
}
