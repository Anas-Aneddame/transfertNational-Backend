package com.example.accountservice.exception;
public class OperationNotFoundException extends RuntimeException {

    public OperationNotFoundException(Long operationId) {
        super("Operation not found with ID: " + operationId);
    }
}
