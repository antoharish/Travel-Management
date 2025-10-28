package com.cts.project.Travelling_package.Controller;

import com.cts.project.Travelling_package.Model.Flight;
import com.cts.project.Travelling_package.Service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping
    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }

    @GetMapping("/search")
    public List<Flight> searchFlights(
            @RequestParam String departureCity,
            @RequestParam String destinationCity,
            @RequestParam String departureTime) {

        LocalTime departureTimeParsed = LocalTime.parse(departureTime);
        return flightService.searchFlights(departureCity, destinationCity, departureTimeParsed);
    }

    @PostMapping
    public Flight createFlight(@RequestBody Flight flight) {
        return flightService.saveFlight(flight);
    }

    @GetMapping("/{id}")
    public Optional<Flight> getFlightById(@PathVariable Long id) {
        return flightService.getFlightById(id);
    }

//    @PutMapping("/{id}")
//    public Flight updateFlight(@PathVariable Long id, @RequestBody Flight flightDetails) {
//        return flightService.updateFlight(id, flightDetails);
//    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFlight(@PathVariable Long id, @RequestBody Flight updatedFlight) {
        try {
            Flight flight = flightService.updateFlight(id, updatedFlight);
            return ResponseEntity.ok(flight);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
    }
}
