package com.example.operationservice.Request;

import com.example.operationservice.Enum.OperationType;
import com.example.operationservice.Enum.TransferType;
import com.example.operationservice.Model.Transfer;
import jakarta.persistence.*;
import lombok.*;


@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class OperationBody {


    private String transferType;
    private String operationType;

}
