package com.umair.banking.audit.enums;

public enum AuditAction {

    CUSTOMER_CREATED,
    CUSTOMER_UPDATED,
    CUSTOMER_DELETED,

    ACCOUNT_CREATED,
    ACCOUNT_ACTIVATED,
    ACCOUNT_FROZEN,
    ACCOUNT_CLOSED,

    DEPOSIT,
    WITHDRAW,
    TRANSFER,
}
