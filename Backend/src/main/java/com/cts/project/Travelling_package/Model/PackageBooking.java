package com.cts.project.Travelling_package.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "package_booking")
    public class PackageBooking implements Booking {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long BookingId;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User user;

        @Column(name = "email")
        private String email;

        private int quantity;
        @ManyToOne
        @JoinColumn(name = "itinerary_id")
        private Itinerary itinerary;

        @Column(name = "booking_date", nullable = false)
        private LocalDate bookingDate;

        private double totalPrice;

        private String status;

        @ManyToOne
        @JoinColumn(name = "package_id")
        private Package PackageId;

        public long getTotalPrice(){
            return (long) (this.totalPrice = itinerary.getTotalPrice());
        }



    }


