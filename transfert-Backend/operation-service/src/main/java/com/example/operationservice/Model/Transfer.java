package com.example.operationservice.Model;


import com.example.operationservice.Enum.TransferStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor  @Builder
public class Transfer {
    @Id
    private String transferReference;
    @Transient
    private Customer sender;

    private Long senderId;
    @Transient
    private Beneficiary receiver;
    private Long receiverId;
    @Enumerated(EnumType.STRING)
    private TransferStatus status;

    private double amount;

    private Date timestamp;


}
