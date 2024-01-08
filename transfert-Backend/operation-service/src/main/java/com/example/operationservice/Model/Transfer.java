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

    @ManyToOne
    private Customer sender;

    @ManyToOne
    private Beneficiary receiver;

    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    private double amount;

    private long timestamp;

    private boolean confirmed;

    private String otp;

    private String codepin;

    private String motif;
    @OneToMany
    private List<Operation> operationList;

}
