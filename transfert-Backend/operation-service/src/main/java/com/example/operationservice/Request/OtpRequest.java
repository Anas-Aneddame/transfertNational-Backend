package com.example.operationservice.Request;


import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class OtpRequest {
    String otp;
    String transferRef;
}
