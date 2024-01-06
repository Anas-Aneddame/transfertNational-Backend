package com.example.operationservice.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TransferRequest {
    TransferBody transferBody;
    OperationBody operationBody;
}
