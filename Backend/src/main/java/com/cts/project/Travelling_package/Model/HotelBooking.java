package com.cts.project.Travelling_package.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hotel_booking")
public class HotelBooking implements Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long BookingId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "email", nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    private double totalPrice;

    private String status;

    public long getTotalPrice(){
        return (long) (this.totalPrice = totalPrice * hotel.getPricePerNight());
    }


}
