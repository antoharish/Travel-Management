package com.cts.project.Travelling_package.Service;

import com.cts.project.Travelling_package.Exception.BadRequestException;
import com.cts.project.Travelling_package.Exception.ResourceNotFoundException;
import com.cts.project.Travelling_package.Model.*;
import com.cts.project.Travelling_package.Model.Package;
import com.cts.project.Travelling_package.PaymentDto.PaymentRequest;
import com.cts.project.Travelling_package.PaymentDto.PaymentResponse;
import com.cts.project.Travelling_package.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
public class PackageBookingService {

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackageBookingRepository packageBookingRepository;

    @Autowired
    private PaymentService paymentService;

    public PaymentResponse bookItinerary(Long userId, String email, Long itineraryId, LocalDate bookingDate, int quantity) {
        // Fetch the itinerary
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new RuntimeException("Itinerary not found with ID: " + itineraryId));

        // Fetch the  package
        Package travelPackage = itinerary.getTravelPackage();
        if (travelPackage == null) {
            throw new RuntimeException("No package associated with the itinerary");
        }

        // Fetch the user
        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Create a new PackageBooking
        PackageBooking booking = new PackageBooking();
        booking.setUser(user);
        booking.setEmail(email);
        booking.setItinerary(itinerary);
        booking.setPackageId(travelPackage); // Set the associated package
        booking.setQuantity(quantity);
        booking.setBookingDate(bookingDate);
        booking.setStatus("Pending");

        // Calculate total price
        double totalPrice = itinerary.getTotalPrice();
        booking.setTotalPrice(totalPrice);

        // Save the booking
        PackageBooking savedBooking = packageBookingRepository.save(booking);

        // Create a payment request
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setName("Itinerary Booking - " + travelPackage.getName());
        paymentRequest.setAmount((long) totalPrice * 100); // Convert to smallest currency unit (e.g., paise for INR)
        paymentRequest.setCurrency("INR");

        // Process the payment
        PaymentResponse paymentResponse = paymentService.createPayment(paymentRequest, userId, PaymentType.ITINERARY, savedBooking);

        // Decrement the package count after successful payment
        if (paymentResponse.isStatus()) {
            decrementPackageCount(travelPackage.getPackageId());
        }

        return paymentResponse;
    }

    public void decrementPackageCount(Long packageId) {
        Package travelPackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with ID: " + packageId));

        if (travelPackage.getPackagesCreated() > 0) {
            travelPackage.setPackagesCreated(travelPackage.getPackagesCreated() - 1);
            packageRepository.save(travelPackage);
        } else {
            throw new BadRequestException("No packages available to decrement.");
        }
    }
}
