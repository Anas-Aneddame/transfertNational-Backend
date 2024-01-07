package com.example.operationservice.Request;

import com.example.operationservice.Model.Beneficiary;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServirBody {
    String transferReference;
    Long agentId;
    Long beneficiaryId;
    String codepin;
}