package com.cts.project.Travelling_package.Controller;

import com.cts.project.Travelling_package.Model.Flight;
import com.cts.project.Travelling_package.Model.FlightAvailability;
import com.cts.project.Travelling_package.Repository.FlightAvailabilityRepository;
import com.cts.project.Travelling_package.Repository.FlightRepository;
import com.cts.project.Travelling_package.Service.FlightAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class FlightAvailabilityController {

    @Autowired
    private FlightAvailabilityRepository availabilityRepository;

    @Autowired
    private FlightAvailabilityService availabilityService;

    @Autowired
    private FlightRepository flightRepository;


    @PostMapping("/addAvailability")
    public ResponseEntity<String> addFlightAvailability(@RequestBody FlightAvailability flightAvailability) {
        if (flightAvailability == null) {
            return ResponseEntity.badRequest().body("FlightAvailability object is null.");
        }

        if (flightAvailability.getFlight() == null || flightAvailability.getFlight().getId() == null) {
            return ResponseEntity.badRequest().body("Flight object or Flight ID is missing.");
        }

        Long flightId = flightAvailability.getFlight().getId();
        Optional<Flight> flightOptional = flightRepository.findById(flightId);
        if (flightOptional.isPresent()) {
            flightAvailability.setFlight(flightOptional.get());
            availabilityRepository.save(flightAvailability);
            return ResponseEntity.ok("Flight availability saved successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Flight ID not found.");
        }
    }


    @GetMapping("/flightAvailability")
    public List<FlightAvailability> getAllFlightAvailability() {
        return availabilityRepository.findAll();
    }


    @GetMapping("/flightAvailability/{id}")
    public ResponseEntity<FlightAvailability> getFlightAvailabilityById(@PathVariable Long id) {
        return availabilityRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/flightAvailability/{id}")
    public ResponseEntity<FlightAvailability> updateFlightAvailability(@PathVariable Long id, @RequestBody FlightAvailability availability) {
        Optional<FlightAvailability> optional = availabilityRepository.findById(id);
        if (optional.isPresent()) {
            FlightAvailability existing = optional.get();
            existing.setFlight(availability.getFlight());
            existing.setDate(availability.getDate());
            existing.setAvailableSeats(availability.getAvailableSeats());
            return ResponseEntity.ok(availabilityRepository.save(existing));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/flightAvailability/{id}")
    public ResponseEntity<String> deleteFlightAvailability(@PathVariable Long id) {
        if (availabilityRepository.existsById(id)) {
            availabilityRepository.deleteById(id);
            return ResponseEntity.ok("Deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FlightAvailability not found.");
        }
    }


    @GetMapping("/searchByDate")
    public List<FlightAvailability> searchFlightsByDate(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam  LocalDate date) {
        return availabilityService.searchFlightsByDate(source, destination, date);
    }
}
