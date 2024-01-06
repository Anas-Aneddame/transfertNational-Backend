package com.example.operationservice.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
}
