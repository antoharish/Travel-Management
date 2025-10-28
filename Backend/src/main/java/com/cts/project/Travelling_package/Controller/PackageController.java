package com.cts.project.Travelling_package.Controller;

import com.cts.project.Travelling_package.Model.FlightAvailability;
import com.cts.project.Travelling_package.Model.Hotel;
import com.cts.project.Travelling_package.Model.Package;
import com.cts.project.Travelling_package.Service.FlightAvailabilityService;
import com.cts.project.Travelling_package.Service.HotelService;
import com.cts.project.Travelling_package.Service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/packages")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @Autowired
    private HotelService hotelService;

//    @Autowired
//    private FlightService flightService;
//
//    @Autowired
//    private FlightAvailability flightAvailability;

    @Autowired
    private FlightAvailabilityService availabilityService;
    //Creating a Package

//    public Package createPackage(@RequestBody Package travelPackage) {
//        return packageService.createPackage(travelPackage);
//    }

    //    @PostMapping("/create-multiple")
//    public ResponseEntity<List<Package>> createMultiplePackages(
//            @RequestBody Package travelPackage,
//            @RequestParam int numberOfPackages) {
//        List<Package> createdPackages = packageService.createMultiplePackages(travelPackage, numberOfPackages);
//        return ResponseEntity.ok(createdPackages);
//    }
    @PostMapping("/createPackages")
    public Package createPackage(@RequestBody Package travelPackage) {
        return packageService.createPackage(travelPackage);
    }

    @GetMapping("/searchPackage")
    public List<Package> searchPackages(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam String location,
            @RequestParam int noOfPeople) {
        return packageService.searchPackages(startDate, endDate, location, noOfPeople);
    }

    @PostMapping("/{packageId}/setPackages")
    public ResponseEntity<Package> setNumberOfPackages(
            @PathVariable Long packageId,
            @RequestParam int numberOfPackages) {
        Package updatedPackage = packageService.setNumberOfPackages(packageId, numberOfPackages);
        return ResponseEntity.ok(updatedPackage);
    }

    //Getting All Packages
    @GetMapping
    public List<Package> getAllPackages() {
        return packageService.getAllPackages();
    }

    //Getting A Package By Its ID
    @GetMapping("/{id}/ta")
    public Package getPackageById(@PathVariable Long id) {
        return packageService.getPackageById(id);
    }

    //Deleting a Package By ID
    @DeleteMapping("/{id}")
    public void deletePackage(@PathVariable Long id) {
        packageService.deletePackage(id);
    }

    @GetMapping("/hotels/{id}")
    public Hotel getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id);
    }


    @GetMapping("/hotels/location/{location}")
    public List<Hotel> getHotelsByLocation(@PathVariable String location) {
        return hotelService.getHotelsByLocation(location);
    }

    @GetMapping("/searchFlights")
    public List<FlightAvailability> searchFlights(
            @RequestParam String departureCity,
            @RequestParam String destinationCity,
            @RequestParam LocalDate departureDate) {
        return availabilityService.searchFlightsByDate(departureCity, destinationCity, departureDate);
    }

    // Endpoint to add a hotel to a package
    @PostMapping("/{packageId}/hotels/{hotelId}/{id}")
    public ResponseEntity<Package> addHotelToPackage(@PathVariable Long packageId, @PathVariable Long hotelId, @PathVariable Long id) {
        try {
            Package updatedPackage = packageService.addHotelToPackage(packageId, hotelId,id);
            return ResponseEntity.ok(updatedPackage);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{packageid}/hotels/{hotelId}/{id}")
    public ResponseEntity<Package> removeHotelFromPackage (@PathVariable Long PackageId, @PathVariable Long hotelId,@PathVariable Long id){
        try {
            Package updatedPackage = packageService.removeHotelFromPackage(PackageId, hotelId,id);
            return ResponseEntity.ok(updatedPackage);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/activities/{activityId}")
    public ResponseEntity<Package> AddActivityToPackage(@PathVariable Long id, @PathVariable Long activityId){
        try {
            Package updatedPackage = packageService.addActivityToPackage(id, activityId);
            return ResponseEntity.ok(updatedPackage);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/activities/{activityId}")
    public ResponseEntity<Package> removeActivityFromPackage (@PathVariable Long id, @PathVariable Long
            activityId)
    {
        try {
            Package updatedPackage = packageService.removeActivityFromPackage(id, activityId);
            return ResponseEntity.ok(updatedPackage);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/addFlightToPackage")
    public ResponseEntity<Package> addFlightToPackage (@RequestParam Long packageId,
                                                       @RequestParam Long flightId,
                                                       @RequestParam LocalDate date) {
        Package updatedPackage = packageService.addFlightToPackage(packageId, flightId, date);
        return ResponseEntity.ok(updatedPackage);
    }

    @DeleteMapping("/{id}/flights/{flightId}")
    public ResponseEntity<Package> removeFlightFromPackage (
            @PathVariable Long packageId,
            @RequestParam Long flightId,
            @RequestParam LocalDate date) {
        Package updatedPackage = packageService.removeFlightFromPackage(packageId, flightId, date);
        return ResponseEntity.ok(updatedPackage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePackage(@PathVariable Long id, @RequestBody Package updatedPackage) {
        try {
            Package updated = packageService.updatePackage(id, updatedPackage);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}