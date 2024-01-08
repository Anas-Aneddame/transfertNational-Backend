package com.example.operationservice.Request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServirOtpRequest {
    String otp;
    String transferRef;
    Long agentId;
}
