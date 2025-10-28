package com.cts.project.Travelling_package.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightBooking implements Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long BookingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    private LocalDate bookingDate;
    private int quantity;
    private String emailId;

    private LocalDate flightDate;

    private int TotalPrice;

    private String status;


}
