package com.cts.project.Travelling_package.ServiceTest;

import com.cts.project.Travelling_package.Model.*;
import com.cts.project.Travelling_package.PaymentDto.PaymentRequest;
import com.cts.project.Travelling_package.PaymentDto.PaymentResponse;
import com.cts.project.Travelling_package.Repository.*;
import com.cts.project.Travelling_package.Service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock private PaymentRepository paymentRepository;
   // @Mock private EmailService emailService;
   // @Mock private PdfGeneratorService pdfGeneratorService;
    @Mock private HotelBookingRepository hotelBookingRepository;
    @Mock private FlightBookingRepository flightBookingRepository;
    @Mock private PackageBookingRepository packageBookingRepository;
    @Mock private UserRepository userRepository;
    @Mock private HotelBooking booking;

    @InjectMocks private PaymentService paymentService;

    private PaymentRequest paymentRequest;
    private User user;
    @BeforeEach
    void setup() {
        paymentRequest = new PaymentRequest( "Test Booking",1000, "INR");
        user = new User();
        user.setUserId(1);
        user.setEmail("test@example.com");

        booking = new HotelBooking();
        booking.setBookingId(1L);
        booking.setQuantity(1);
    }

    @Test
    void testGetPaymentBySessionId() {
        Payment payment = new Payment();
        payment.setSessionId("abc123");

        when(paymentRepository.findBySessionId("abc123")).thenReturn(Optional.of(payment));

        Payment result = paymentService.getPaymentBySessionId("abc123");

        assertEquals("abc123", result.getSessionId());
    }

    //This will fail as we are not mocking the API
    @Test
    void testCreatePaymentSuccess() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenReturn(new Payment());

        PaymentResponse response = paymentService.createPayment(paymentRequest, 1L, PaymentType.HOTEL, booking);

        assertTrue(response.isStatus());
        assertNotNull(response.getSessionId());
    }

    @Test
    void testUpdatePaymentStatus_Hotel() {
        Payment payment = new Payment();
        payment.setBookingId(1L);
        payment.setUser(user);

        HotelBooking hotelBooking = new HotelBooking();
        hotelBooking.setBookingId(1L);

        when(paymentRepository.findBySessionId("abc123")).thenReturn(Optional.of(payment));
        when(hotelBookingRepository.findById(1L)).thenReturn(Optional.of(hotelBooking));

        paymentService.updatePaymentStatus(PaymentType.HOTEL, "abc123", "Processed");

        verify(paymentRepository).save(any(Payment.class));
    //    verify(emailService).sendPaymentConfirmationEmail(anyString(), any(), any());
    }
}
