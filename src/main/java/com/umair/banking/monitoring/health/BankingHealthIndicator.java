package com.umair.banking.monitoring.health;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class BankingHealthIndicator implements HealthIndicator {


    @Override
    public Health health() {
        return Health.up()
                .withDetail("service", "Banking System")
                .withDetail("message", "Banking system is operational")
                .build();
    }
}
