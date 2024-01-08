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
    @GeneratedValue
    private String transferReference;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Customer sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Beneficiary receiver;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    private double amount;

    private Date timestamp;

    @OneToMany
    private List<Operation> operationList;

}