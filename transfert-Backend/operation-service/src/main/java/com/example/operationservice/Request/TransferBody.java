package com.example.operationservice.Request;

import com.example.operationservice.Enum.TransferStatus;
import com.example.operationservice.Model.Beneficiary;
import com.example.operationservice.Model.Customer;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TransferBody {

    private String senderId;

    private String receiverId;
    private String status;
    private double amount;
}
