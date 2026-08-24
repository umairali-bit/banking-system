package com.umair.banking.aop.aspect;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PerformanceAspect {

    @Around("@annotation(com.umair.banking.aop.annotation.LogExecutionTime)")
    public Object measureExecutionTime(
            ProceedingJoinPoint joinPoint)
            throws Throwable {

        long start = System.currentTimeMillis();

        try{
            return joinPoint.proceed();
        } finally {

            long end = System.currentTimeMillis();
            long executionTime = end - start;

            log.info("Method {} executed in {} ms", joinPoint.getSignature().toShortString(), executionTime);

        }
    }
}
