package com.cts.project.Travelling_package.Service;

import com.cts.project.Travelling_package.Model.Flight;
import com.cts.project.Travelling_package.Model.FlightAvailability;
import com.cts.project.Travelling_package.Repository.FlightAvailabilityRepository;
import com.cts.project.Travelling_package.Repository.FlightRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightAvailabilityService {

    @Autowired
    private FlightAvailabilityRepository flightAvailabilityRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Transactional
    public List<FlightAvailability> searchFlightsByDate(String source, String destination, LocalDate date) {

//        flightAvailabilityRepository.deleteByDateBefore(LocalDate.now());


        List<FlightAvailability> availabilities = flightAvailabilityRepository
                .findByFlightDepartureCityAndFlightDestinationCityAndDate(source, destination, date);


        List<Flight> flights = flightRepository.findByDepartureCityAndDestinationCity(source, destination);

        List<FlightAvailability> newAvailabilities = new ArrayList<>();

        for (Flight flight : flights) {

            boolean exists = availabilities.stream()
                    .anyMatch(fa -> fa.getFlight().getId().equals(flight.getId()));

            if (!exists) {
                FlightAvailability availability = FlightAvailability.builder()
                        .flight(flight)
                        .date(date)
                        .availableSeats(flight.getTotalSeats())
                        .build();

                FlightAvailability saved = flightAvailabilityRepository.save(availability);
                availabilities.add(saved);
                newAvailabilities.add(saved);
            }
        }

        return availabilities;
    }
}
