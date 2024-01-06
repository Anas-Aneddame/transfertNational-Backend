package com.example.accountservice.model;

import com.example.accountservice.Enum.OperationType;
import com.example.accountservice.Enum.TransferType;
import jakarta.persistence.*;

import java.util.Date;
@Entity
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Transfer transferReference;
    @Enumerated(EnumType.STRING)
    private TransferType transferType;
    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    private Date timestamp;


}