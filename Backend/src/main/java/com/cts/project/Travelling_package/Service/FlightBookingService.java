package com.cts.project.Travelling_package.Service;

import com.cts.project.Travelling_package.Model.*;
import com.cts.project.Travelling_package.PaymentDto.PaymentRequest;
import com.cts.project.Travelling_package.PaymentDto.PaymentResponse;
import com.cts.project.Travelling_package.Repository.FlightAvailabilityRepository;
import com.cts.project.Travelling_package.Repository.FlightBookingRepository;
import com.cts.project.Travelling_package.Repository.FlightRepository;
import com.cts.project.Travelling_package.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class FlightBookingService {

    @Autowired
    private FlightBookingRepository flightBookingRepository;

    @Autowired
    private FlightAvailabilityRepository flightAvailabilityRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

//    @Autowired
//    private EmailService emailService;

    public PaymentResponse bookFlight(Long userId, String email, Long flightId, LocalDate flightDate, int quantity) {
        FlightAvailability availability = flightAvailabilityRepository
                .findByFlightIdAndDate(flightId, flightDate)
                .orElseThrow(() -> new RuntimeException("No availability found for the selected flight and date"));

        if (availability.getAvailableSeats() < quantity) {
            throw new RuntimeException("Not enough seats available for the selected date");
        }

        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        int totalPrice = flight.getPrice() * quantity;

        FlightBooking booking = new FlightBooking();
        booking.setUser(user);
        booking.setEmailId(email);
        booking.setFlight(flight);
        booking.setFlightDate(flightDate);
        booking.setQuantity(quantity);
        booking.setTotalPrice(totalPrice);
        booking.setStatus("Pending");

        FlightBooking savedBooking = flightBookingRepository.save(booking);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setName("Flight Booking - " + flight.getDepartureCity() + " to " + flight.getDestinationCity());
        paymentRequest.setAmount((long)totalPrice * 100);
        paymentRequest.setCurrency("INR");
        return paymentService.createPayment(paymentRequest, userId, PaymentType.FLIGHT, savedBooking);

    }


    public List<FlightBooking> getAllBookings() {
        return flightBookingRepository.findAll();
    }

    public FlightBooking getBookingById(Long id) {
        return flightBookingRepository.findById(id).orElse(null);
    }

    public FlightBooking updateBooking(Long id, FlightBooking updatedBooking) {
        Optional<FlightBooking> existingBookingOpt = flightBookingRepository.findById(id);
        if (existingBookingOpt.isPresent()) {
            FlightBooking booking = existingBookingOpt.get();
            booking.setUser(updatedBooking.getUser());
            booking.setEmailId(updatedBooking.getEmailId());
            booking.setFlight(updatedBooking.getFlight());
            booking.setFlightDate(updatedBooking.getFlightDate());
            booking.setQuantity(updatedBooking.getQuantity());
            booking.setTotalPrice(updatedBooking.getTotalPrice());
            return flightBookingRepository.save(booking);
        }
        return null;
    }

    public boolean deleteBooking(Long id) {
        Optional<FlightBooking> booking = flightBookingRepository.findById(id);
        if (booking.isPresent()) {
            flightBookingRepository.deleteById(id);
            return true;
        }
        return false;
    }
}