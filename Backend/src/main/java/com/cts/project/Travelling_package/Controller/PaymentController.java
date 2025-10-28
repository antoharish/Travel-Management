package com.cts.project.Travelling_package.Controller;

import com.cts.project.Travelling_package.Model.*;
import com.cts.project.Travelling_package.PaymentDto.PaymentResponse;
import com.cts.project.Travelling_package.Service.*;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private InvoiceService invoiceService;

//    @Autowired
//    private EmailService emailService;
    @Autowired
    private HotelBookingService hotelBookingService;

    @Autowired
    private PackageBookingService packageBookingService;

    @Autowired
    private FlightBookingService flightBookingService;

    @PostMapping("/package")
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
    @PostMapping("/hotels")
    public ResponseEntity<?> bookHotel(@RequestParam Long userId,
                                       @RequestParam String email,
                                       @RequestParam Long hotelId,
                                       @RequestParam String bookingDate,
                                       @RequestParam int quantity) {
        try {
            LocalDate date = LocalDate.parse(bookingDate);
            PaymentResponse booking = hotelBookingService.bookHotel(userId, email, hotelId, date, quantity);
            return ResponseEntity.ok(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/flights")
    public ResponseEntity<?> bookFlight(@RequestParam Long userId,
                                        @RequestParam Long flightId,
                                        @RequestParam String flightDate,
                                        @RequestParam int quantity,
                                        @RequestParam String emailId) {
        LocalDate flightDateParsed = LocalDate.parse(flightDate); // Parsing the flight date
        PaymentResponse paymentResponse = flightBookingService.bookFlight(userId, emailId, flightId, flightDateParsed, quantity);
        return ResponseEntity.ok(paymentResponse);
    }

//    @GetMapping("/payment/success/{sessionId}")
//    public ResponseEntity<String> paymentSuccess(@PathVariable String sessionId) {
//        try {
//            Session session = Session.retrieve(sessionId);
//            String paymentTypeStr = session.getMetadata().get("paymentType");
//            PaymentType paymentType = PaymentType.valueOf(paymentTypeStr);
//
//            paymentService.updatePaymentStatus(paymentType, sessionId, "Processed");
//
//            Invoice invoice = invoiceService.generateInvoice(sessionId);
//            Payment payment = paymentService.getPaymentBySessionId(sessionId);
//
//            // Redirect to the frontend success page with sessionId as a query parameter
//            String successUrl = "http://localhost:4200/payment-success" + sessionId;
//            //String successUrl = "http://localhost:4200/payment-success?sessionId=" + sessionId;
//            return ResponseEntity.status(HttpStatus.FOUND).header("Location", successUrl).build();
//        } catch (StripeException e) {
//            throw new RuntimeException("Failed to retrieve session: " + e.getMessage());
//        }
//    }






}