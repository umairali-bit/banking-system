package com.umair.banking.exception;

public class BusinessAccountNotFoundException extends RuntimeException {
    public BusinessAccountNotFoundException(String message) {
        super(message);
    }
}
