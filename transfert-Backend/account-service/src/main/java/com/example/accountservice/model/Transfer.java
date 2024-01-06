package com.example.accountservice.model;

import com.example.accountservice.Enum.TransferStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transfer {
    @Id
    private String transferReference;
    @Transient
    private Customer sender;

    @Transient
    private Beneficiary receiver;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    private double amount;

    private Date timestamp;

    @OneToMany
    private List<Operation> operationList;

}