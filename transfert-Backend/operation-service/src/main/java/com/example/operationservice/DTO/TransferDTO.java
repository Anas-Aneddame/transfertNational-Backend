package com.example.operationservice.DTO;

import com.example.operationservice.Enum.TransferStatus;
import com.example.operationservice.Model.Beneficiary;
import com.example.operationservice.Model.Customer;
import com.example.operationservice.Model.Operation;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Builder @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TransferDTO {
    private String transferReference;

    private Long sender;

    private Long receiver;

    private TransferStatus status;

    private double amount;

    private long timestamp;

    private boolean confirmed;

    private String otp;

    private String codepin;

    private String motif;

}
