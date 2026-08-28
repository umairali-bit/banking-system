package com.umair.banking.monitoring.annotation;


import com.umair.banking.transaction.enums.TransactionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackTransactionMetric {

    TransactionType value();
}
