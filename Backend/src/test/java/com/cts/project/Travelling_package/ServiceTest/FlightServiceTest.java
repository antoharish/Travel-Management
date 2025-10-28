package com.cts.project.Travelling_package.ServiceTest;

import com.cts.project.Travelling_package.Model.Flight;
import com.cts.project.Travelling_package.Repository.FlightRepository;
import com.cts.project.Travelling_package.Service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    private Flight flight;

    @BeforeEach
    public void setUp() {
        flight = new Flight(); // Initialize the flight object
        flight.setId(1L); // Set the ID
        flight.setDepartureCity("New York");
        flight.setDestinationCity("Los Angeles");
        flight.setDepartureTime(LocalTime.of(10, 0));
        flight.setArrivalTime(LocalTime.of(14, 0));
        flight.setTotalSeats(150);
        flight.setPrice(300);
    }

    @Test
    public void testSearchFlights() {
        when(flightRepository.findByDepartureCityAndDestinationCityAndDepartureTime(
                "New York", "Los Angeles", LocalTime.of(10, 0)))
                .thenReturn(Arrays.asList(flight));

        List<Flight> flights = flightService.searchFlights("New York", "Los Angeles", LocalTime.of(10, 0));
        assertEquals(1, flights.size());
        assertEquals(flight, flights.get(0));
    }

    @Test
    public void testSaveFlight() {
        when(flightRepository.save(flight)).thenReturn(flight);

        Flight savedFlight = flightService.saveFlight(flight);
        assertNotNull(savedFlight);
        assertEquals(flight.getId(), savedFlight.getId());
    }

    @Test
    public void testGetFlightById() {
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        Optional<Flight> foundFlight = flightService.getFlightById(1L);
        assertTrue(foundFlight.isPresent());
        assertEquals(flight.getId(), foundFlight.get().getId());
    }

    @Test
    public void testUpdateFlight() {
        // Use a real Flight object, not a mock, since mock is returning default values rather than the one we set.
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setDepartureCity("Old City");
        flight.setDestinationCity("Old Destination");
        flight.setDepartureTime(LocalTime.of(8, 0));
        flight.setArrivalTime(LocalTime.of(10, 0));
        flight.setTotalSeats(150);
        flight.setPrice(300);

        Flight updatedFlightDetails = new Flight();
        updatedFlightDetails.setDepartureCity("San Francisco");
        updatedFlightDetails.setDestinationCity("Chicago");
        updatedFlightDetails.setDepartureTime(LocalTime.of(12, 0));
        updatedFlightDetails.setArrivalTime(LocalTime.of(16, 0));
        updatedFlightDetails.setTotalSeats(200);
        updatedFlightDetails.setPrice(400);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(Flight.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Flight updatedFlight = flightService.updateFlight(1L, updatedFlightDetails);

        assertNotNull(updatedFlight);
        assertEquals("San Francisco", updatedFlight.getDepartureCity());
        assertEquals("Chicago", updatedFlight.getDestinationCity());
        assertEquals(LocalTime.of(12, 0), updatedFlight.getDepartureTime());
        assertEquals(LocalTime.of(16, 0), updatedFlight.getArrivalTime());
        assertEquals(400, updatedFlight.getPrice());
    }

    @Test
    public void testDeleteFlight() {
        doNothing().when(flightRepository).deleteById(1L);

        flightService.deleteFlight(1L);
        verify(flightRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetAllFlights() {
        when(flightRepository.findAll()).thenReturn(Arrays.asList(flight));

        List<Flight> flights = flightService.getAllFlights();
        assertEquals(1, flights.size());
        assertEquals(flight, flights.get(0));
    }
}
