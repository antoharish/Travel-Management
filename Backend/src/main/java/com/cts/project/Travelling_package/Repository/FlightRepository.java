package com.cts.project.Travelling_package.Repository;


import com.cts.project.Travelling_package.Model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByDepartureCityAndDestinationCityAndDepartureTime(
            String departureCity, String destinationCity, LocalTime departureTime);
    List<Flight> findByDepartureCityAndDestinationCity(String departureCity, String destinationCity);
}
