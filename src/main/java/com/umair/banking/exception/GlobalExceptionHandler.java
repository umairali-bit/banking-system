package com.umair.banking.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex,
                                                                            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );


    }


    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleAccountNotFoundException(AccountNotFoundException ex,
                                                                           HttpServletRequest request) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(InvalidAccountStateException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidAccountStateException(InvalidAccountStateException ex,
                                                                               HttpServletRequest request) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI()
        );

    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                                                  HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                message,
                request.getRequestURI()
        );
    }


    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateEmail(
            DuplicateEmailException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(DuplicatePhoneNumberException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicatePhone(
            DuplicatePhoneNumberException ex,
            HttpServletRequest request) {

        return buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            String path
    ) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );

        return ResponseEntity.status(status).body(apiErrorResponse);

    }

    @ExceptionHandler(InsufficientFundsExceptions.class)
    public ResponseEntity<ApiErrorResponse> handleInsufficientFundsException(InsufficientFundsExceptions ex,
                                                                             HttpServletRequest request) {

        return buildResponse(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleTransactionNotFoundException(TransactionNotFoundException ex,
                                                                               HttpServletRequest request) {

        return buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleRoleNotFoundException(RoleNotFoundException ex,
                                                                        HttpServletRequest request) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex,
                                                                             HttpServletRequest request) {
        return buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(AccessDeniedException ex,
                                                                        HttpServletRequest request) {
        return buildResponse(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(DuplicateUserNameException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateUserNameException(DuplicateUserNameException ex,
                                                                             HttpServletRequest request) {
        return buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI()
        );
    }
}
