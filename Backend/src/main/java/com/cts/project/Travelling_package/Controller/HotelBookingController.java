package com.cts.project.Travelling_package.Controller;

import com.cts.project.Travelling_package.Model.Hotel;
import com.cts.project.Travelling_package.Model.HotelBooking;
import com.cts.project.Travelling_package.Dto.PaymentResponse;
import com.cts.project.Travelling_package.Service.HotelBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class HotelBookingController {

    @Autowired
    private HotelBookingService hotelBookingService;



    @GetMapping
    public List<HotelBooking> getAllBookings() {
        return hotelBookingService.getAllBookings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelBooking> getBookingById(@PathVariable Long id) {
        HotelBooking booking = hotelBookingService.getBookingById(id);
        if (booking != null) {
            return ResponseEntity.ok(booking);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelBooking> updateBooking(@PathVariable Long id, @RequestBody HotelBooking updatedBooking) {
        HotelBooking booking = hotelBookingService.updateBooking(id, updatedBooking);
        if (booking != null) {
            return ResponseEntity.ok(booking);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        boolean isDeleted = hotelBookingService.deleteBooking(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}