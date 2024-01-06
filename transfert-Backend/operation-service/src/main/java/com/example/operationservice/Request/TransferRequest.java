package com.example.operationservice.Request;

import com.example.operationservice.Enum.FraisType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TransferRequest {
    TransferBody transferBody;
    OperationBody operationBody;
    @Enumerated(EnumType.STRING)
    FraisType fraisType;
}
