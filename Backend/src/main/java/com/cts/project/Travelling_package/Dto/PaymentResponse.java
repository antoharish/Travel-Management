package com.cts.project.Travelling_package.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private boolean status;
    private String message;
    private String sessionId;
    private String sessionUrl;
}
