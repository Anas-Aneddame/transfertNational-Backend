package com.example.accountservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDTO {

    private String firstName;
    private String lastName;
    private String password;
    private String role;
    private Long customerId;


    public UserRegistrationDTO() {
        this.role = "customer";
        this.password ="password";
    }



}
