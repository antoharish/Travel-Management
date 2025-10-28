package com.cts.project.Travelling_package.Repository;

import com.cts.project.Travelling_package.Model.Flight;
import com.cts.project.Travelling_package.Model.FlightBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightBookingRepository extends JpaRepository<FlightBooking, Long> {
    Optional<Flight> findByFlight_Id(Long flightId);


}
