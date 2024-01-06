package com.example.accountservice.exception;
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long CustomerId){
        super("Could not found the customer with id "+ CustomerId);
    }
}