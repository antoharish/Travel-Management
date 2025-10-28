package com.cts.project.Travelling_package.Service;

import com.cts.project.Travelling_package.Model.Flight;
import com.cts.project.Travelling_package.Repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    public List<Flight> searchFlights(String departureCity, String destinationCity, LocalTime departureTime) {
        return flightRepository.findByDepartureCityAndDestinationCityAndDepartureTime(
                departureCity, destinationCity, departureTime);
    }

    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }

    public Flight updateFlight(Long id, Flight updatedFlight) {
        return flightRepository.findById(id).map(existingFlight -> {
            existingFlight.setDepartureCity(updatedFlight.getDepartureCity());
            existingFlight.setDestinationCity(updatedFlight.getDestinationCity());
            existingFlight.setDepartureTime(updatedFlight.getDepartureTime());
            existingFlight.setArrivalTime(updatedFlight.getArrivalTime());
            existingFlight.setPrice(updatedFlight.getPrice());
            return flightRepository.save(existingFlight);
        }).orElseThrow(() -> new RuntimeException("Flight not found with ID: " + id));
    }

    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }
}
