package com.umair.banking.exception;

public class  InsufficientFundsExceptions extends RuntimeException{
    public InsufficientFundsExceptions(String message) {
        super(message);
    }
}
