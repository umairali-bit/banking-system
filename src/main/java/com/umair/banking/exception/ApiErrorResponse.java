package com.umair.banking.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse(

        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path

) {
}
