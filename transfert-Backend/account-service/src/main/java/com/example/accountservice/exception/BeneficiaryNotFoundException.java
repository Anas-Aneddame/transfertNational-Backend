package com.example.accountservice.exception;

public class BeneficiaryNotFoundException extends RuntimeException{
    public BeneficiaryNotFoundException(Long id) {
        super("Prospect not found with id: " + id);
    }
}
