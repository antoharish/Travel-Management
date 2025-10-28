package com.cts.project.Travelling_package.Service;
import com.cts.project.Travelling_package.Exception.BadRequestException;
import com.cts.project.Travelling_package.Exception.ResourceNotFoundException;
import com.cts.project.Travelling_package.Model.*;
import com.cts.project.Travelling_package.Model.Package;
import com.cts.project.Travelling_package.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cts.project.Travelling_package.Repository.HotelAvailabilityRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private FlightAvailabilityRepository flightAvailabilityRepository;

    @Autowired
    private HotelAvailabilityRepository hotelAvailabilityRepository;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    public Package createPackage(Package travelPackage) {
        travelPackage.setPackagetotalPrice(travelPackage.getPrice());
        travelPackage.setIncludedHotels(new ArrayList<>());
        travelPackage.setIncludedFlights(new ArrayList<>());
        travelPackage.setActivities(new ArrayList<>());

        int totalPeople = travelPackage.getNoOfPeople() * travelPackage.getPackagesCreated();

        // Check availability of hotels and flights
        if (!checkRoomAvailability(travelPackage, totalPeople) || !checkFlightAvailability(travelPackage, totalPeople)) {
            throw new BadRequestException("Not enough availability for hotels or flights.");
        }

        // Create the packages
        Package createdPackage = packageRepository.save(travelPackage);
        return packageRepository.save(createdPackage);
    }

    private boolean checkRoomAvailability(Package travelPackage, int totalPeople) {
        for (Hotel hotel : travelPackage.getIncludedHotels()) {
            if (hotel.getRoomsAvailable() < totalPeople) {
                return false;
            }
        }
        return true;
    }

    private boolean checkFlightAvailability(Package travelPackage, int totalPeople) {
        for (Flight flight : travelPackage.getIncludedFlights()) {
            FlightAvailability availability = flightAvailabilityRepository.findByFlightIdAndDate(
                            flight.getId(), travelPackage.getStartDate())
                    .orElseThrow(() -> new ResourceNotFoundException("Flight availability not found."));
            if (availability.getAvailableSeats() < totalPeople) {
                return false;
            }
        }
        return true;
    }

    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }

    public List<Package> searchPackages(LocalDate startDate, LocalDate endDate, String location, int noOfPeople) {
        return packageRepository.findByLocationAndStartDateBetweenAndNoOfPeopleGreaterThanEqual(
                location, startDate, endDate, noOfPeople);
    }

    public Package getPackageById(Long id) {
        return packageRepository.findById(id).orElse(null);
    }

    public Package addActivityToPackage(Long id, Long activityId) {
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id " + id));
        Package.Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with id " + activityId));
        pkg.getActivities().add(activity);
        calculateTotalPrice(pkg, true, activity.getPrice());
        return packageRepository.save(pkg);
    }

    public Package removeActivityFromPackage(Long id, Long activityId) {
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id " + id));
        Package.Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with id " + activityId));
        pkg.getActivities().remove(activity);
        calculateTotalPrice(pkg, true, activity.getPrice());
        return packageRepository.save(pkg);
    }

    public void deletePackage(Long id) {
        packageRepository.deleteById(id);
    }

    public List<FlightAvailability> searchFlights(String departureCity, String destinationCity, LocalDate departureDate) {
        return flightAvailabilityRepository.findByFlightDepartureCityAndFlightDestinationCityAndDate(departureCity, destinationCity, departureDate);
    }

    public Package addFlightToPackage(Long packageId, Long flightId, LocalDate date) {
        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with ID: " + packageId));
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with ID: " + flightId));
        FlightAvailability flightAvailability = flightAvailabilityRepository.findByFlightIdAndDate(flightId, date)
                .orElseThrow(() -> new ResourceNotFoundException("Flight availability not found for flight ID: " + flightId + " on date: " + date));
        if (flightAvailability.getAvailableSeats() < pkg.getNoOfPeople()) {
            throw new BadRequestException("Not enough seats available on flight ");
        }
        flightAvailability.setAvailableSeats(flightAvailability.getAvailableSeats() - pkg.getNoOfPeople());
        flightAvailabilityRepository.save(flightAvailability);
        if (pkg.getIncludedFlights() == null) {
            pkg.setIncludedFlights(new ArrayList<>());
        }
        pkg.getIncludedFlights().add(flight);
        calculateTotalPrice(pkg, true, flight.getPrice() * pkg.getNoOfPeople());
        return packageRepository.save(pkg);
    }

    public Package removeFlightFromPackage(Long packageId, Long flightId, LocalDate date) {
        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id " + packageId));
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with id " + flightId));
        FlightAvailability flightAvailability = flightAvailabilityRepository.findByFlightIdAndDate(flightId, date)
                .orElseThrow(() -> new ResourceNotFoundException("Flight availability not found for flight ID: " + flightId + " on date: " + date));
        if (!pkg.getIncludedFlights().remove(flight)) {
            throw new BadRequestException("Flight not included in the package");
        }
        flightAvailability.setAvailableSeats(flightAvailability.getAvailableSeats() + pkg.getNoOfPeople());
        flightAvailabilityRepository.save(flightAvailability);
        calculateTotalPrice(pkg, false, flight.getPrice() * pkg.getNoOfPeople());
        return packageRepository.save(pkg);
    }

    public Package setNumberOfPackages(Long packageId, int numberOfPackages) {
        Package travelPackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with ID: " + packageId));
        travelPackage.setPackagesCreated(numberOfPackages);
        return packageRepository.save(travelPackage);
    }

    public void decrementPackageCount(Long packageId) {
        Package travelPackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with ID: " + packageId));
        if (travelPackage.getPackagesCreated() > 0) {
            travelPackage.setPackagesCreated(travelPackage.getPackagesCreated() - 1);
            packageRepository.save(travelPackage);
        } else {
            throw new BadRequestException("No packages available to decrement.");
        }
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id).orElse(null);
    }

    public List<Hotel> searchHotelsByLocation(String location) {
        return hotelRepository.findByLocation(location);
    }

    public Package addHotelToPackage(Long packageId, Long hotelId, Long id) {
        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id " + packageId));
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id " + hotelId));
        HotelAvailability hotelAvailability = hotelAvailabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HotelAvailability not found with id " + id));
        hotelAvailability.setCapacity(hotelAvailability.getCapacity() - pkg.getNoOfPeople());
        hotelAvailabilityRepository.save(hotelAvailability);
        hotelRepository.save(hotel);
        pkg.getIncludedHotels().add(hotel);
        calculateTotalPrice(pkg, true, hotel.getPricePerNight() * pkg.getNoOfDays());
        return packageRepository.save(pkg);
    }

    public Package removeHotelFromPackage(Long packageId, Long hotelId, Long id) {
        Package pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id " + packageId));
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id " + hotelId));
        HotelAvailability hotelAvailability = hotelAvailabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HotelAvailability not found with id " + id));
        hotelAvailability.setCapacity(hotelAvailability.getCapacity() + pkg.getNoOfPeople());
        hotelAvailabilityRepository.save(hotelAvailability);
        pkg.getIncludedHotels().remove(hotel);
        hotelRepository.save(hotel);
        calculateTotalPrice(pkg, false, hotel.getPricePerNight() * pkg.getNoOfDays());
        return packageRepository.save(pkg);
    }

    public boolean checkRoomAvailability(Package travelPackage) {
        for (Hotel hotel : travelPackage.getIncludedHotels()) {
            if (hotel.getRoomsAvailable() < travelPackage.getNoOfPeople()) {
                return false;
            }
        }
        return true;
    }

    public void calculateTotalPrice(Package travelPackage, boolean isAdding, double priceChange) {
        double totalPrice = travelPackage.getPackagetotalPrice();
        if (isAdding) {
            totalPrice += priceChange;
        } else {
            totalPrice -= priceChange;
        }
        travelPackage.setPackagetotalPrice(totalPrice);
    }

    public Package updatePackage(Long id, Package updatedPackage) {
        return packageRepository.findById(id).map(existingPackage -> {
            existingPackage.setName(updatedPackage.getName());
            existingPackage.setPrice(updatedPackage.getPrice());
            existingPackage.setNoOfDays(updatedPackage.getNoOfDays());
            existingPackage.setNoOfPeople(updatedPackage.getNoOfPeople());
            existingPackage.setLocation(updatedPackage.getLocation());
            existingPackage.setStartDate(updatedPackage.getStartDate());
            existingPackage.setEndDate(updatedPackage.getEndDate());
            existingPackage.setPackagesCreated(updatedPackage.getPackagesCreated());
            existingPackage.setIncludedHotels(updatedPackage.getIncludedHotels());
            existingPackage.setIncludedFlights(updatedPackage.getIncludedFlights());
            existingPackage.setActivities(updatedPackage.getActivities());
            existingPackage.setPackagetotalPrice(updatedPackage.getPackagetotalPrice());
            return packageRepository.save(existingPackage);
        }).orElseThrow(() -> new ResourceNotFoundException("Package not found with ID: " + id));
    }
}





//package com.cts.project.Travelling_package.Service;
//import com.cts.project.Travelling_package.Exception.BadRequestException;
//import com.cts.project.Travelling_package.Exception.ResourceNotFoundException;
//import com.cts.project.Travelling_package.Model.*;
//import com.cts.project.Travelling_package.Model.Package;
//import com.cts.project.Travelling_package.Repository.*;
//import jakarta.transaction.Transactional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import com.cts.project.Travelling_package.Repository.HotelAvailabilityRepository;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class PackageService {
//
//    @Autowired
//    private PackageRepository packageRepository;
//
//    @Autowired
//    private ActivityRepository activityRepository;
//    @Autowired
//    private FlightRepository flightRepository;
//    @Autowired
//    private HotelRepository hotelRepository;
//    @Autowired
//    private FlightAvailabilityRepository flightAvailabilityRepository;
//
//    @Autowired
//    private HotelAvailabilityRepository hotelAvailabilityRepository;
//
//    @Autowired
//    private PdfGeneratorService pdfGeneratorService;
//
////    public Package createPackage(Package travelPackage) {
////        return packageRepository.save(travelPackage);
////    }
//
//
//    public Package createPackage(Package travelPackage) {
//
//        travelPackage.setPackagetotalPrice(travelPackage.getPrice());
//        travelPackage.setIncludedHotels(new ArrayList<>());
//        travelPackage.setIncludedFlights(new ArrayList<>());
//        travelPackage.setActivities(new ArrayList<>());
//
//        int totalPeople = travelPackage.getNoOfPeople() * travelPackage.getPackagesCreated();
//
//        // Check availability of hotels and flights
//
//        if (!checkRoomAvailability(travelPackage, totalPeople) || !checkFlightAvailability(travelPackage, totalPeople)) {
//
//            throw new RuntimeException("Not enough availability for hotels or flights.");
//
//        }
//
//        // Create the packages
//
//        Package createdPackage = packageRepository.save(travelPackage);
//
//        return packageRepository.save(createdPackage);
//
//    }
//
//    private boolean checkRoomAvailability(Package travelPackage, int totalPeople) {
//
//        for (Hotel hotel : travelPackage.getIncludedHotels()) {
//
//            if (hotel.getRoomsAvailable() < totalPeople) {
//
//                return false;
//
//            }
//
//        }
//
//        return true;
//
//    }
//
//    private boolean checkFlightAvailability(Package travelPackage, int totalPeople) {
//
//        for (Flight flight : travelPackage.getIncludedFlights()) {
//
//            FlightAvailability availability = flightAvailabilityRepository.findByFlightIdAndDate(
//
//                            flight.getId(), travelPackage.getStartDate())
//
//                    .orElseThrow(() -> new RuntimeException("Flight availability not found."));
//
//            if (availability.getAvailableSeats() < totalPeople) {
//
//                return false;
//
//            }
//
//        }
//
//        return true;
//
//    }
//    public List<Package> getAllPackages() {
//
//        return packageRepository.findAll();
//
//    }
//
//    public List<Package> searchPackages(LocalDate startDate, LocalDate endDate, String location, int noOfPeople) {
//
//        return packageRepository.findByLocationAndStartDateBetweenAndNoOfPeopleGreaterThanEqual(
//
//                location, startDate, endDate, noOfPeople);
//
//    }
//
//
//    public Package getPackageById(Long id) {
//
//        return packageRepository.findById(id).orElse(null);
//
//    }
//
//
//    public Package addActivityToPackage(Long id, Long activityId) {
//
//        Package pkg = packageRepository.findById(id)
//
//                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id " + id));
//
//        Package.Activity activity = activityRepository.findById(activityId)
//
//                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with id " + activityId));
//
//        pkg.getActivities().add(activity);
//
//        calculateTotalPrice(pkg, true, activity.getPrice());
//
//        return packageRepository.save(pkg);
//
//    }
//
//    public Package removeActivityFromPackage(Long id, Long activityId) {
//
//        Package pkg = packageRepository.findById(id)
//
//                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id " + id));
//
//        Package.Activity activity = activityRepository.findById(activityId)
//
//                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with id " + activityId));
//
//        pkg.getActivities().remove(activity);
//
//        calculateTotalPrice(pkg, true, activity.getPrice());
//
//        return packageRepository.save(pkg);
//
//    }
//
//    public void deletePackage(Long id) {
//
//        packageRepository.deleteById(id);
//
//    }
//
//
//    public List<FlightAvailability> searchFlights(String departureCity, String destinationCity, LocalDate departureDate) {
//
//        return flightAvailabilityRepository.findByFlightDepartureCityAndFlightDestinationCityAndDate(  departureCity, destinationCity, departureDate);}
//
//    public Package addFlightToPackage(Long packageId, Long flightId, LocalDate date) throws BadRequestException {
//
//        // Fetch the package
//
//        Package pkg = packageRepository.findById(packageId)
//
//                .orElseThrow(() -> new ResourceNotFoundException("Package not found with ID: " + packageId));
//
//        // Fetch the flight
//
//        Flight flight = flightRepository.findById(flightId)
//
//                .orElseThrow(() -> new ResourceNotFoundException("Flight not found with ID: " + flightId));
//
//        // Fetch flight availability for the given flight and date
//
//        FlightAvailability flightAvailability = flightAvailabilityRepository.findByFlightIdAndDate(flightId, date)
//
//                .orElseThrow(() -> new RuntimeException("Flight availability not found for flight ID: " + flightId + " on date: " + date));
//
//        // Check if there are enough available seats
//
//        if (flightAvailability.getAvailableSeats() < pkg.getNoOfPeople()) {
//
//            throw new BadRequestException("Not enough seats available on flight ");
//
//        }
//
//        // Deduct the required seats from availability
//
//        flightAvailability.setAvailableSeats(flightAvailability.getAvailableSeats() - pkg.getNoOfPeople());
//
//        flightAvailabilityRepository.save(flightAvailability);
//
//        // Ensure the package's flight list is initialized
//
//        if (pkg.getIncludedFlights() == null) {
//
//            pkg.setIncludedFlights(new ArrayList<>());
//
//        }
//
//        // Add the flight to the package
//
//        pkg.getIncludedFlights().add(flight);
//
//        // Update the package's total price
//
//        calculateTotalPrice(pkg, true, flight.getPrice() * pkg.getNoOfPeople());
//
//        // Save and return the updated package
//
//        return packageRepository.save(pkg);
//
//    }
//
//    public Package removeFlightFromPackage(Long packageId, Long flightId, LocalDate date) {
//
//        // Fetch the package
//
//        Package pkg = packageRepository.findById(packageId)
//
//                .orElseThrow(() -> new RuntimeException("Package not found with id " + packageId));
//
//        // Fetch the flight
//
//        Flight flight = flightRepository.findById(flightId)
//
//                .orElseThrow(() -> new RuntimeException("Flight not found with id " + flightId));
//
//        // Fetch flight availability for the given flight and date
//
//        FlightAvailability flightAvailability = flightAvailabilityRepository.findByFlightIdAndDate(flightId, date)
//
//                .orElseThrow(() -> new RuntimeException("Flight availability not found for flight ID: " + flightId + " on date: " + date));
//
//        // Remove the flight from the package
//
//        if (!pkg.getIncludedFlights().remove(flight)) {
//
//            throw new RuntimeException("Flight not included in the package");
//
//        }
//
//        // Update the available seats in FlightAvailability
//
//        flightAvailability.setAvailableSeats(flightAvailability.getAvailableSeats() + pkg.getNoOfPeople());
//
//        flightAvailabilityRepository.save(flightAvailability);
//
//        // Update the package's total price
//
//        calculateTotalPrice(pkg, false, flight.getPrice() * pkg.getNoOfPeople());
//
//        // Save and return the updated package
//
//        return packageRepository.save(pkg);
//
//    }
//
//    public Package setNumberOfPackages(Long packageId, int numberOfPackages) {
//
//        Package travelPackage = packageRepository.findById(packageId)
//
//                .orElseThrow(() -> new RuntimeException("Package not found with ID: " + packageId));
//
//        travelPackage.setPackagesCreated(numberOfPackages);
//
//        return packageRepository.save(travelPackage);
//
//    }
//
//    public void decrementPackageCount(Long packageId) {
//
//        Package travelPackage = packageRepository.findById(packageId)
//
//                .orElseThrow(() -> new RuntimeException("Package not found with ID: " + packageId));
//
//        if (travelPackage.getPackagesCreated() > 0) {
//
//            travelPackage.setPackagesCreated(travelPackage.getPackagesCreated() - 1);
//
//            packageRepository.save(travelPackage);
//
//        } else {
//
//            throw new RuntimeException("No packages available to decrement.");
//
//        }
//
//    }
//
//
//    public Hotel getHotelById(Long id) {
//
//        return hotelRepository.findById(id).orElse(null);
//
//    }
//
//
//
//    public List<Hotel> searchHotelsByLocation(String location) {
//
//        return hotelRepository.findByLocation(location);
//
//    }
//
//    public Package addHotelToPackage(Long packageId, Long hotelId,Long id) {
//
//        Package pkg = packageRepository.findById(packageId)
//
//                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id " + packageId));
//
//        Hotel hotel = hotelRepository.findById(hotelId)
//
//                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id " + hotelId));
//
//        HotelAvailability hotelAvailability = hotelAvailabilityRepository.findById(id)
//
//                .orElseThrow(() -> new ResourceNotFoundException("HotelAvailability not found with id " + id));
//
//        hotelAvailability.setCapacity(hotelAvailability.getCapacity() - pkg.getNoOfPeople());
//
//        hotelAvailabilityRepository.save(hotelAvailability);
//
//        hotelRepository.save(hotel);
//
//        double totalHotelPrice = hotel.getPricePerNight();
//
//        pkg.getIncludedHotels().add(hotel);
//
//        calculateTotalPrice(pkg, true, hotel.getPricePerNight() * pkg.getNoOfDays());
//
//        return packageRepository.save(pkg);
//
//    }
//
//    public Package removeHotelFromPackage(Long packageId, Long hotelId,Long id) {
//
//        Package pkg = packageRepository.findById(packageId)
//
//                .orElseThrow(() -> new ResourceNotFoundException("Package not found with id " + packageId));
//
//        Hotel hotel = hotelRepository.findById(hotelId)
//
//                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id " + hotelId));
//
//        HotelAvailability hotelAvailability = hotelAvailabilityRepository.findById(id)
//
//                .orElseThrow(() -> new ResourceNotFoundException("HotelAvailability not found with id " + id));
//
//        hotelAvailability.setCapacity(hotelAvailability.getCapacity() + pkg.getNoOfPeople());
//
//        hotelAvailabilityRepository.save(hotelAvailability);
//
//        pkg.getIncludedHotels().remove(hotel);
//
//        hotelRepository.save(hotel);// Removes the hotel
//
//        calculateTotalPrice(pkg, false, hotel.getPricePerNight() * pkg.getNoOfDays()); // Updates price dynamically
//
//        return packageRepository.save(pkg);
//
//    }
//
//
//
//
//    public boolean checkRoomAvailability(Package travelPackage) {
//
//        for (Hotel hotel : travelPackage.getIncludedHotels()) {
//
//            if (hotel.getRoomsAvailable() < travelPackage.getNoOfPeople()) {
//
//                return false;
//
//            }
//
//        }
//
//        return true;
//
//    }
//
//
//    public void calculateTotalPrice(Package travelPackage, boolean isAdding, double priceChange) {
//
//        // Start with current total price instead of recalculating everything
//
//        double totalPrice = travelPackage.getPackagetotalPrice();
//
//        if (isAdding) {
//
//            totalPrice += priceChange; // Increase price when adding
//
//        } else {
//
//            totalPrice -= priceChange; // Decrease price when removing
//
//        }
//
//        travelPackage.setPackagetotalPrice(totalPrice); // Update final price
//
//    }
//
//
//    // Updates dynamically
//    public Package updatePackage(Long id, Package updatedPackage) {
//        return packageRepository.findById(id).map(existingPackage -> {
//            existingPackage.setName(updatedPackage.getName());
//            existingPackage.setPrice(updatedPackage.getPrice());
//            existingPackage.setNoOfDays(updatedPackage.getNoOfDays());
//            existingPackage.setNoOfPeople(updatedPackage.getNoOfPeople());
//            existingPackage.setLocation(updatedPackage.getLocation());
//            existingPackage.setStartDate(updatedPackage.getStartDate());
//            existingPackage.setEndDate(updatedPackage.getEndDate());
//            existingPackage.setPackagesCreated(updatedPackage.getPackagesCreated());
//            existingPackage.setIncludedHotels(updatedPackage.getIncludedHotels());
//            existingPackage.setIncludedFlights(updatedPackage.getIncludedFlights());
//            existingPackage.setActivities(updatedPackage.getActivities());
//            existingPackage.setPackagetotalPrice(updatedPackage.getPackagetotalPrice());
//            return packageRepository.save(existingPackage);
//        }).orElseThrow(() -> new RuntimeException("Package not found with ID: " + id));
//    }
//
//}
