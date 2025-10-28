package com.cts.project.Travelling_package.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private long amount;
    private long bookingId;
    private String name;
    private String currency;
}
