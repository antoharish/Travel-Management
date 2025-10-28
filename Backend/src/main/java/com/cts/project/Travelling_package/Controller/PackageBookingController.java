package com.cts.project.Travelling_package.Controller;

import com.cts.project.Travelling_package.Model.HotelBooking;
import com.cts.project.Travelling_package.Model.PackageBooking;
import com.cts.project.Travelling_package.PaymentDto.PaymentResponse;
import com.cts.project.Travelling_package.Service.PackageBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

public class PackageBookingController {

    @Autowired
    private PackageBookingService packageBookingService;
    @PostMapping
    public ResponseEntity<?> bookItinerary(@RequestParam Long userId,
                                       @RequestParam String email,
                                       @RequestParam Long itineraryId,
                                       @RequestParam String bookingDate,
                                       @RequestParam int quantity) {

        try {
            LocalDate date = LocalDate.parse(bookingDate);
           PaymentResponse booking = packageBookingService.bookItinerary(userId, email, itineraryId, date, quantity);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

