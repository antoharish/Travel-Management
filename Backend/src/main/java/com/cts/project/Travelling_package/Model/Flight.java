package com.cts.project.Travelling_package.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flight  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departureCity;
    private String destinationCity;

    private LocalTime departureTime;
    private LocalTime arrivalTime;

    private int totalSeats;
    private int price;
}
