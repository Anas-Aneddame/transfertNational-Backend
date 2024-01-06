package com.example.accountservice.exception;
public class TransferNotFoundException extends RuntimeException {

    public TransferNotFoundException(Long transferReference) {
        super("Transfer not found with reference: " + transferReference);
    }
}