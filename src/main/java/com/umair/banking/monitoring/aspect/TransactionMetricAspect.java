package com.umair.banking.monitoring.aspect;


import com.umair.banking.monitoring.annotation.TrackTransactionMetric;
import com.umair.banking.monitoring.metrics.BankingMetrics;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TransactionMetricAspect {

    private final BankingMetrics bankingMetrics;

    @AfterReturning(pointcut = "@annotation(trackTransactionMetric)")
    public void recordSuccess(TrackTransactionMetric trackTransactionMetric) {

        bankingMetrics.incrementTransaction(
                trackTransactionMetric.value(), "SUCCESS"
        );

    }

    @AfterThrowing(
            pointcut = "@annotation(trackTransactionMetric)",
            throwing = "exception"
    )
    public void recordFailure(TrackTransactionMetric trackTransactionMetric, Exception exception) {

        bankingMetrics.incrementTransaction(
                trackTransactionMetric.value(), "FAILD"
        );
    }
}
