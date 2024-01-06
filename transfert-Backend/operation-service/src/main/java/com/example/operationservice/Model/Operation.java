package com.example.operationservice.Model;

import com.example.operationservice.Enum.OperationType;
import com.example.operationservice.Enum.TransferType;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
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

    private Long timestamp;


}
