package com.example.operationservice.Request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExtourneRequest {
    String transferReference;
    Long agentId;
    String motif;
}
