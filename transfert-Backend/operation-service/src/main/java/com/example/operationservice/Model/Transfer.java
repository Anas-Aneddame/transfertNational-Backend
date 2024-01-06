package com.example.operationservice.Model;


import com.example.operationservice.Enum.TransferStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor  @Builder
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

    private long timestamp;

    @OneToMany
    private List<Operation> operationList;

}
