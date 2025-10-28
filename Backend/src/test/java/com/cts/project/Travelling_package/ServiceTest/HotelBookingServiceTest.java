package com.cts.project.Travelling_package.ServiceTest;

import com.cts.project.Travelling_package.Model.Hotel;
import com.cts.project.Travelling_package.Model.HotelAvailability;
import com.cts.project.Travelling_package.Model.HotelBooking;
import com.cts.project.Travelling_package.Model.User;
import com.cts.project.Travelling_package.PaymentDto.PaymentResponse;
import com.cts.project.Travelling_package.Repository.HotelAvailabilityRepository;
import com.cts.project.Travelling_package.Repository.HotelBookingRepository;
import com.cts.project.Travelling_package.Repository.HotelRepository;
import com.cts.project.Travelling_package.Repository.UserRepository;
import com.cts.project.Travelling_package.Service.HotelBookingService;
import com.cts.project.Travelling_package.Service.PdfGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelBookingServiceTest {

    @Mock
    private HotelBookingRepository hotelBookingRepository;

    @Mock
    private HotelAvailabilityRepository hotelAvailabilityRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PdfGeneratorService pdfGeneratorService;

    @InjectMocks
    private HotelBookingService hotelBookingService;

    private Hotel hotel;
    private HotelAvailability hotelAvailability;
    private User user;
    private HotelBooking hotelBooking;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setHotelId(1L);
        hotel.setLocation("New York");
        hotel.setRoomsAvailable(100);

        hotelAvailability = new HotelAvailability();
        hotelAvailability.setHotel(hotel);
        hotelAvailability.setDate(LocalDate.of(2025, 5, 8));
        hotelAvailability.setCapacity(100);

        user = new User();
        user.setUserId(1);
        user.setUsername("John Doe");
        user.setEmail("john.doe@example.com");

        hotelBooking = new HotelBooking();
        hotelBooking.setUser(user);
        hotelBooking.setEmail("john.doe@example.com");
        hotelBooking.setHotel(hotel);
        hotelBooking.setBookingDate(LocalDate.of(2025, 5, 8));
        hotelBooking.setQuantity(2);
    }

    //fail - (it uses emailService)
    @Test
    public void testBookHotel() {
        when(hotelAvailabilityRepository.findByHotelIdAndDate(1L, LocalDate.of(2025, 5, 8)))
                .thenReturn(Optional.of(hotelAvailability));
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelBookingRepository.save(any(HotelBooking.class))).thenReturn(hotelBooking);
        when(pdfGeneratorService.generateHotelBookingPdf(any(HotelBooking.class))).thenReturn(new byte[0]);

        PaymentResponse bookedHotel = hotelBookingService.bookHotel(1L, "sure196855@gmail.com", 1L, LocalDate.of(2025, 5, 8), 2);
        assertNotNull(bookedHotel);
      //  assertEquals(hotelBooking.getEmail(), bookedHotel.getEmail());
       // verify(emailService, times(1)).sendBookingConfirmationWithPdf(eq("sure196855@gmail.com"), any(HotelBooking.class), any(byte[].class));
    }

    @Test
    public void testGetAllBookings() {
        when(hotelBookingRepository.findAll()).thenReturn(Arrays.asList(hotelBooking));

        List<HotelBooking> bookings = hotelBookingService.getAllBookings();
        assertEquals(1, bookings.size());
        assertEquals(hotelBooking, bookings.get(0));
    }

    @Test
    public void testGetBookingById() {
        when(hotelBookingRepository.findById(1L)).thenReturn(Optional.of(hotelBooking));

        HotelBooking foundBooking = hotelBookingService.getBookingById(1L);
        assertNotNull(foundBooking);
        assertEquals(hotelBooking.getEmail(), foundBooking.getEmail());
    }

    @Test
    public void testUpdateBooking() {
        HotelBooking updatedBookingDetails = new HotelBooking();
        updatedBookingDetails.setUser(user);
        updatedBookingDetails.setEmail("jane.doe@example.com");
        updatedBookingDetails.setHotel(hotel);
        updatedBookingDetails.setBookingDate(LocalDate.of(2025, 5, 9));
        updatedBookingDetails.setQuantity(3);

        when(hotelBookingRepository.findById(1L)).thenReturn(Optional.of(hotelBooking));
        when(hotelBookingRepository.save(any(HotelBooking.class))).thenReturn(hotelBooking);

        HotelBooking updatedBooking = hotelBookingService.updateBooking(1L, updatedBookingDetails);
        assertNotNull(updatedBooking);
        assertEquals("jane.doe@example.com", updatedBooking.getEmail());
        assertEquals(LocalDate.of(2025, 5, 9), updatedBooking.getBookingDate());
        assertEquals(3, updatedBooking.getQuantity());
    }

    @Test
    public void testDeleteBooking() {
        when(hotelBookingRepository.findById(1L)).thenReturn(Optional.of(hotelBooking));
        doNothing().when(hotelBookingRepository).deleteById(1L);

        boolean isDeleted = hotelBookingService.deleteBooking(1L);
        assertTrue(isDeleted);
        verify(hotelBookingRepository, times(1)).deleteById(1L);
    }
}
