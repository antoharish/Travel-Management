package com.cts.project.Travelling_package.ServiceTest;

import com.cts.project.Travelling_package.Model.Hotel;
import com.cts.project.Travelling_package.Model.HotelAvailability;
import com.cts.project.Travelling_package.Repository.HotelAvailabilityRepository;
import com.cts.project.Travelling_package.Repository.HotelRepository;
import com.cts.project.Travelling_package.Service.HotelAvailabilityService;
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
public class HotelAvailabilityServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelAvailabilityRepository availabilityRepository;

    @InjectMocks
    private HotelAvailabilityService hotelAvailabilityService;

    private Hotel hotel;
    private HotelAvailability hotelAvailability;

    @BeforeEach
    public void setUp() {
        hotel = new Hotel();
        hotel.setHotelId(1L);
        hotel.setName("Grand Hotel");
        hotel.setLocation("New York");
        hotel.setRoomsAvailable(100);
        hotel.setRating(4.5);
        hotel.setPricePerNight(200.0);

        hotelAvailability = new HotelAvailability();
        hotelAvailability.setDate(LocalDate.of(2025, 5, 8));
        hotelAvailability.setCapacity(100);
        hotelAvailability.setHotel(hotel);
    }

    @Test
    public void testGetAvailableHotels() {
        when(hotelRepository.findByLocation("New York")).thenReturn(Arrays.asList(hotel));
        when(availabilityRepository.findByHotel_HotelIdAndDate(1L, LocalDate.of(2025, 5, 8)))
                .thenReturn(Optional.of(hotelAvailability));

        List<HotelAvailability> availableHotels = hotelAvailabilityService.getAvailableHotels("New York", LocalDate.of(2025, 5, 8));
        assertEquals(1, availableHotels.size());
        assertEquals(hotelAvailability, availableHotels.get(0));
    }

    @Test
    public void testCreateHotelAvailability() {
        when(availabilityRepository.save(hotelAvailability)).thenReturn(hotelAvailability);

        HotelAvailability savedAvailability = hotelAvailabilityService.createHotelAvailability(hotelAvailability);
        assertNotNull(savedAvailability);
        assertEquals(hotelAvailability.getHotel().getHotelId(), savedAvailability.getHotel().getHotelId());
    }

    @Test
    public void testGetHotelAvailability() {
        when(availabilityRepository.findById(1L)).thenReturn(Optional.of(hotelAvailability));

        HotelAvailability foundAvailability = hotelAvailabilityService.getHotelAvailability(1L);
        assertNotNull(foundAvailability);
        assertEquals(hotelAvailability.getHotel().getHotelId(), foundAvailability.getHotel().getHotelId());
    }

    @Test
    public void testUpdateHotelAvailability() {
        HotelAvailability updatedAvailabilityDetails = new HotelAvailability();
        updatedAvailabilityDetails.setDate(LocalDate.of(2025, 5, 9));
        updatedAvailabilityDetails.setCapacity(80);
        updatedAvailabilityDetails.setHotel(hotel);

        when(availabilityRepository.findById(1L)).thenReturn(Optional.of(hotelAvailability));
        when(availabilityRepository.save(hotelAvailability)).thenReturn(hotelAvailability);

        HotelAvailability updatedAvailability = hotelAvailabilityService.updateHotelAvailability(1L, updatedAvailabilityDetails);
        assertNotNull(updatedAvailability);
        assertEquals(LocalDate.of(2025, 5, 9), updatedAvailability.getDate());
        assertEquals(80, updatedAvailability.getCapacity());
    }

    @Test
    public void testDeleteHotelAvailability() {
        doNothing().when(availabilityRepository).deleteById(1L);

        hotelAvailabilityService.deleteHotelAvailability(1L);
        verify(availabilityRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetAllHotelAvailability() {
        when(availabilityRepository.findAll()).thenReturn(Arrays.asList(hotelAvailability));

        List<HotelAvailability> allAvailability = hotelAvailabilityService.getAllHotelAvailability();
        assertEquals(1, allAvailability.size());
        assertEquals(hotelAvailability, allAvailability.get(0));
    }

    @Test
    public void testDeletePastHotelAvailability() {
        when(availabilityRepository.findByDateBefore(LocalDate.now())).thenReturn(Arrays.asList(hotelAvailability));
        doNothing().when(availabilityRepository).delete(hotelAvailability);

        hotelAvailabilityService.deletePastHotelAvailability();
        verify(availabilityRepository, times(1)).delete(hotelAvailability);
    }
}
