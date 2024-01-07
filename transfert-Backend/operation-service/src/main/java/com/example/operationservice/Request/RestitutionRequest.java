package com.example.operationservice.Request;


import com.example.operationservice.Enum.OperationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RestitutionRequest {
    String transferReference;
    @Enumerated(EnumType.STRING)
    OperationType operationType;
    String motif;
}
