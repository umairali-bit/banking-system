package com.umair.banking.monitoring.metrics;

import com.umair.banking.transaction.enums.TransactionType;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class BankingMetrics {

    private final MeterRegistry meterRegistry;

    public BankingMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }


    public void incrementTransaction(TransactionType transactionType, String status) {

        meterRegistry.counter("banking.transactions.total",
                "type", transactionType.name(),
                       "status", status) .increment();

    }
}



