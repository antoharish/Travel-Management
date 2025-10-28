package com.cts.project.Travelling_package.PaymentDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private String name;
    private long amount;
    private String currency;
}