package com.cts.project.Travelling_package.Controller;

import com.cts.project.Travelling_package.Model.FlightBooking;
import com.cts.project.Travelling_package.PaymentDto.PaymentResponse;
import com.cts.project.Travelling_package.Service.FlightBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/flightBookings")
public class FlightBookingController {

    @Autowired
    private FlightBookingService flightBookingService;

    // Create a flight booking with payment


    // Handle payment success
//    @GetMapping("/payment/success")
//    public ResponseEntity<String> handlePaymentSuccess(@RequestParam String sessionId) {
//        try {
//            flightBookingService.handlePaymentSuccess(sessionId);
//            return ResponseEntity.ok("Payment successful! Confirmation email and e-ticket have been sent.");
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(500).body("Error processing payment: " + e.getMessage());
//        }
//    }

    // Get all flight bookings
    @GetMapping
    public List<FlightBooking> getAllFlightBookings() {
        return flightBookingService.getAllBookings();
    }

    // Get a flight booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<FlightBooking> getFlightBookingById(@PathVariable Long id) {
        FlightBooking flightBooking = flightBookingService.getBookingById(id);
        if (flightBooking != null) {
            return ResponseEntity.ok(flightBooking);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update a flight booking
    @PutMapping("/{id}")
    public ResponseEntity<FlightBooking> updateFlightBooking(@PathVariable Long id, @RequestBody FlightBooking flightBookingDetails) {
        FlightBooking updatedBooking = flightBookingService.updateBooking(id, flightBookingDetails);
        if (updatedBooking != null) {
            return ResponseEntity.ok(updatedBooking);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a flight booking
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlightBooking(@PathVariable Long id) {
        boolean isDeleted = flightBookingService.deleteBooking(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}