package com.example.accountservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Long CustomerId;
    private String FirstName;
    private String LastName;
    private String CNE;
    private String Email;
    private String Phone;
    private Double Balance;
    private String AccountType;


    public Customer(Long customerId, String firstName, String lastName, String CNE, String email, String phone, Double balance, String accountType) {
        CustomerId = customerId;
        FirstName = firstName;
        LastName = lastName;
        this.CNE = CNE;
        Email = email;
        Phone = phone;
        this.Balance = balance;
        AccountType = accountType;
    }

    public Customer() {
    }

    public Long getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(Long customerId) {
        CustomerId = customerId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getCNE() {
        return CNE;
    }

    public void setCNE(String CNE) {
        this.CNE = CNE;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public Double getBalance() {
        return Balance;
    }

    public void setBalance(Double balance) {
        this.Balance = balance;
    }

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }
}
