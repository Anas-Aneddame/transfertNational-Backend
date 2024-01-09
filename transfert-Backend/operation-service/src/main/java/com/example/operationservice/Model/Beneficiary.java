package com.example.operationservice.Model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class Beneficiary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    @ManyToOne
    Customer customer;
}
