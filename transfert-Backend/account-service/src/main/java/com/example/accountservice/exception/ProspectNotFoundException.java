package com.example.accountservice.exception;

public class ProspectNotFoundException extends RuntimeException{
    public ProspectNotFoundException(Long ProspectId) {
        super("Prospect not found with id: " + ProspectId);
    }
}
