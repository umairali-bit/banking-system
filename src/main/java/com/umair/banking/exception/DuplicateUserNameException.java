package com.umair.banking.exception;

public class DuplicateUserNameException extends RuntimeException {
    public DuplicateUserNameException(String message) {
        super(message);
    }
}
