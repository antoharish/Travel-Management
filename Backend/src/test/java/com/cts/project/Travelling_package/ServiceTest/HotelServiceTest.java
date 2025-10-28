//package com.cts.project.Travelling_package.ServiceTest;
//
//
//
//import com.cts.project.Travelling_package.Model.Hotel;
//import com.cts.project.Travelling_package.Repository.HotelRepository;
//import com.cts.project.Travelling_package.Service.HotelService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
package com.cts.project.Travelling_package.ServiceTest;

import com.cts.project.Travelling_package.Model.Hotel;
import com.cts.project.Travelling_package.Repository.HotelRepository;
import com.cts.project.Travelling_package.Service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    private Hotel hotel;

    @BeforeEach
    public void setUp() {
        hotel = new Hotel();
        hotel.setHotelId(1L);
        hotel.setName("Grand Hotel");
        hotel.setLocation("New York");
        hotel.setRoomsAvailable(100);
        hotel.setRating(4.5);
        hotel.setPricePerNight(200.0);
    }

    @Test
    public void testGetAllHotels() {
        when(hotelRepository.findAll()).thenReturn(Arrays.asList(hotel));

        List<Hotel> hotels = hotelService.getAllHotels();
        assertEquals(1, hotels.size());
        assertEquals(hotel, hotels.get(0));
    }

    @Test
    public void testGetHotelById() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        Hotel foundHotel = hotelService.getHotelById(1L);
        assertNotNull(foundHotel);
        assertEquals(hotel.getName(), foundHotel.getName());
    }

    @Test
    public void testCreateHotel() {
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        Hotel createdHotel = hotelService.createHotel(hotel);
        assertNotNull(createdHotel);
        assertEquals(hotel.getName(), createdHotel.getName());
    }

    @Test
    public void testUpdateHotel() {
        Hotel updatedHotelDetails = new Hotel();
        updatedHotelDetails.setName("Updated Hotel");
        updatedHotelDetails.setLocation("Los Angeles");
        updatedHotelDetails.setRoomsAvailable(150);
        updatedHotelDetails.setRating(4.8);
        updatedHotelDetails.setPricePerNight(250.0);

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        Hotel updatedHotel = hotelService.updateHotel(1L, updatedHotelDetails);
        assertNotNull(updatedHotel);
        assertEquals("Updated Hotel", updatedHotel.getName());
        assertEquals("Los Angeles", updatedHotel.getLocation());
        assertEquals(150, updatedHotel.getRoomsAvailable());
        assertEquals(4.8, updatedHotel.getRating());
        assertEquals(250.0, updatedHotel.getPricePerNight());
    }

    @Test
    public void testDeleteHotel() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        doNothing().when(hotelRepository).deleteById(1L);

        boolean isDeleted = hotelService.deleteHotel(1L);
        assertTrue(isDeleted);
        verify(hotelRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetHotelsByLocation() {
        when(hotelRepository.findByLocation("New York")).thenReturn(Arrays.asList(hotel));

        List<Hotel> hotels = hotelService.getHotelsByLocation("New York");
        assertEquals(1, hotels.size());
        assertEquals(hotel, hotels.get(0));
    }
}
