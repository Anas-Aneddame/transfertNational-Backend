package com.example.operationservice.Reponse;

import com.example.operationservice.DTO.TransferDTO;
import com.example.operationservice.Model.Beneficiary;
import com.example.operationservice.Model.Customer;
import com.example.operationservice.Model.Operation;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransferBackofficeResponse {
    Customer customer;
    Beneficiary beneficiary;
    Operation operation;
    TransferDTO transferDTO;
}
