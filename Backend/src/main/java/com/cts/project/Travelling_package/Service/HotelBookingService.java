package com.cts.project.Travelling_package.Service;

import com.cts.project.Travelling_package.Model.*;
import com.cts.project.Travelling_package.PaymentDto.PaymentRequest;
import com.cts.project.Travelling_package.PaymentDto.PaymentResponse;
import com.cts.project.Travelling_package.Repository.HotelAvailabilityRepository;
import com.cts.project.Travelling_package.Repository.HotelBookingRepository;
import com.cts.project.Travelling_package.Repository.HotelRepository;
import com.cts.project.Travelling_package.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class HotelBookingService {

    @Autowired
    private HotelBookingRepository hotelBookingRepository;

    @Autowired
    private HotelAvailabilityRepository hotelAvailabilityRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private EmailService emailService;
//
    @Autowired
    private PaymentService paymentService;
//
//    @Autowired
//    private PdfGeneratorService pdfGeneratorService;

    public PaymentResponse bookHotel(Long userId, String email, Long hotelId, LocalDate bookingDate, int quantity) {
        HotelAvailability availability = hotelAvailabilityRepository
                .findByHotelIdAndDate(hotelId, bookingDate)
                .orElseThrow(() -> new RuntimeException("No availability found for the selected hotel and date"));

        if (availability.getCapacity() < quantity) {
            throw new RuntimeException("Not enough capacity available for the selected date");
        }

        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        availability.setCapacity(availability.getCapacity() - quantity);
        hotelAvailabilityRepository.save(availability);

        HotelBooking booking = new HotelBooking();
        booking.setUser(user);
        booking.setEmail(email);
        booking.setHotel(hotel);
        booking.setBookingDate(bookingDate);
        booking.setQuantity(quantity);
        booking.setStatus("Pending");


        double totalPrice = hotel.getPricePerNight() * quantity;
        booking.setTotalPrice(totalPrice);

        HotelBooking savedBooking = hotelBookingRepository.save(booking);
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setName("Hotel Booking - " + hotel.getName());
        paymentRequest.setAmount((long) totalPrice * 100);
        paymentRequest.setCurrency("INR");
        return paymentService.createPayment(paymentRequest, userId, PaymentType.HOTEL, savedBooking);
    }


    public List<HotelBooking> getAllBookings() {
        return hotelBookingRepository.findAll();
    }

    public HotelBooking getBookingById(Long id) {
        return hotelBookingRepository.findById(id).orElse(null);
    }

    public HotelBooking updateBooking(Long id, HotelBooking updatedBooking) {
        Optional<HotelBooking> existingBookingOpt = hotelBookingRepository.findById(id);
        if (existingBookingOpt.isPresent()) {
            HotelBooking booking = existingBookingOpt.get();
            booking.setUser(updatedBooking.getUser());
            booking.setEmail(updatedBooking.getEmail());
            booking.setHotel(updatedBooking.getHotel());
            booking.setBookingDate(updatedBooking.getBookingDate());
            booking.setQuantity(updatedBooking.getQuantity());
            booking.setTotalPrice(updatedBooking.getTotalPrice());
            return hotelBookingRepository.save(booking);
        }
        return null;
    }

    public boolean deleteBooking(Long id) {
        Optional<HotelBooking> booking = hotelBookingRepository.findById(id);
        if (booking.isPresent()) {
            hotelBookingRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
