package com.cts.project.Travelling_package.Repository;

// FlightAvailabilityRepository.java


import com.cts.project.Travelling_package.Model.Flight;
import com.cts.project.Travelling_package.Model.FlightAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FlightAvailabilityRepository extends JpaRepository<FlightAvailability, Long> {

    // Find availability for a given flight
//    List<FlightAvailability> findByFlight(Flight flight);

    List<FlightAvailability> findByFlightDepartureCityAndFlightDestinationCityAndDate(
            String departureCity, String destinationCity, LocalDate departureDate);


    void deleteByDateBefore(LocalDate date);
    Optional<FlightAvailability> findByFlightIdAndDate(Long flightId, LocalDate date);
}
