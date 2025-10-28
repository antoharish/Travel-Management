package com.cts.project.Travelling_package.PaymentDto;

import jakarta.persistence.Column;
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

    @Column(length = Integer.MAX_VALUE,nullable = false, unique = true)

    private String sessionId;
    private String sessionUrl;
}