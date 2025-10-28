package com.cts.project.Travelling_package.Service;

import com.cts.project.Travelling_package.Model.*;
import com.cts.project.Travelling_package.Model.Package;
import com.cts.project.Travelling_package.PaymentDto.PaymentRequest;
import com.cts.project.Travelling_package.PaymentDto.PaymentResponse;
import com.cts.project.Travelling_package.Repository.*;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.TaxRate;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    @Autowired
    private PaymentRepository paymentRepository;

//    @Autowired
//    private EmailService emailService;

//    @Autowired
//    private PdfGeneratorService pdfGeneratorService;

    @Autowired
    private HotelBookingRepository hotelBookingRepository;

    @Autowired
    private FlightBookingRepository flightBookingRepository;

    @Autowired
    private PackageBookingRepository packageBookingRepository;

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private PackageService packageService;


    @Autowired
    private UserRepository userRepository;

    long bookingIdGeneration;




    public static final Map<String, Double> TAX_RATES = Map.of(
            "INR", 0.18,
            "USD", 0.07,
            "EUR", 0.20,
            "JPY", 0.10
    );
    public Payment getPaymentBySessionId(String sessionId) {
        return paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Payment not found with session ID: " + sessionId));
    }


    public <T extends Booking> PaymentResponse createPayment(PaymentRequest paymentRequest, Long userId, PaymentType paymentType, T booking) {
        Stripe.apiKey = secretKey;

        long beforeTaxAmount = paymentRequest.getAmount();
        long totalRate = 0 ;
        long totalAmount = 0;
//        String currency = paymentRequest.getCurrency() == null ? "INR" : paymentRequest.getCurrency();
        String currency = "INR";
        double taxRate = TAX_RATES.getOrDefault(currency, 0.0);
        totalRate = (long) (beforeTaxAmount * taxRate);
        totalAmount = (long) (beforeTaxAmount + totalRate);
        paymentRequest.setAmount(totalAmount);

        User user = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(paymentRequest.getName())
                .setDescription("Amount: " + beforeTaxAmount * 0.01 + " with Tax " + taxRate*100 + "% = " + totalAmount * 0.01 )
                .build();

        SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(paymentRequest.getCurrency())
                .setUnitAmount(totalAmount)
                .setProductData(productData)
                .build();

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity((long) booking.getQuantity())
                .setPriceData(priceData)
                .build();



        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/payment-success")// Redirect to the Angular success page
                .setCancelUrl("http://localhost:4200/payment-cancel")   // Redirect to the Angular cancel page
                .addLineItem(lineItem)
                .putMetadata("paymentType", paymentType.toString())
                .build();

        try {
            Session session = Session.create(params);
            System.out.println("Created Stripe session with ID: " + session.getId());

            Payment payment = new Payment();
            payment.setUser(user);
            payment.setAmount(totalAmount);
            payment.setStatus("Processed");
            payment.setPaymentMethod("Stripe");
            payment.setSessionId(session.getId());
            payment.setSessionUrl(session.getUrl());
            payment.setPaymentType(paymentType);
            payment.setBookingId(booking.getBookingId());
            paymentRepository.save(payment);
//            String toEmail = user.getEmail();
//
//            if ("Processed".equals(status)) {
//                byte[] pdfBytes = pdfGeneratorService.generatePaymentPdf(payment);
//                emailService.sendPaymentConfirmationEmail(toEmail,payment, pdfBytes);
//
//            }


            return PaymentResponse.builder()
                    .status(true)
                    .message("Payment session created successfully")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();

        } catch (StripeException e) {
            throw new RuntimeException("Failed to create payment session: " + e.getMessage());
        }
    }




















    //testing
    public void updatePaymentStatus(PaymentType paymentType, String sessionId, String status) {
        try {


            Payment payment = paymentRepository.findBySessionId(sessionId)
                    .orElseThrow(() -> new RuntimeException("Payment not found with session ID: " + sessionId));

            if (paymentType == PaymentType.HOTEL) {

                HotelBooking booking = hotelBookingRepository.findById(payment.getBookingId())
                        .orElseThrow(() -> new RuntimeException("Booking not found for payment"));
                booking.setStatus("Processed");
                hotelBookingRepository.save(booking);
            }
            else if  (paymentType == PaymentType.FLIGHT) {

                FlightBooking booking = flightBookingRepository.findById(payment.getBookingId())
                        .orElseThrow(() -> new RuntimeException("Booking not found for payment"));
                booking.setStatus("Processed");
                flightBookingRepository.save(booking);
            } else if  (paymentType == PaymentType.ITINERARY) {

                PackageBooking booking = packageBookingRepository.findById(payment.getBookingId())
                        .orElseThrow(() -> new RuntimeException("Booking not found for payment"));
                booking.setStatus("Processed");
                Long packageId = booking.getPackageId().getPackageId();
                packageBookingRepository.save(booking);
            }

            payment.setStatus(status);
            payment.setBookingId(bookingIdGeneration);
            paymentRepository.save(payment);

            User user = payment.getUser();
//            String toEmail = user.getEmail();
//
//            if ("Processed".equals(status)) {
//                byte[] pdfBytes = pdfGeneratorService.generatePaymentPdf(payment);
//                emailService.sendPaymentConfirmationEmail(toEmail,payment, pdfBytes);
//
//            }
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while updating payment status or sending email: " + e.getMessage());
        }
    }



}
