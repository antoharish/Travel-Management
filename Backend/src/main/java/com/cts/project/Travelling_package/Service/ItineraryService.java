//package com.cts.project.Travelling_package.Service;
//
//import com.cts.project.Travelling_package.Model.Package.Activity;
//import com.cts.project.Travelling_package.Model.Itinerary;
//import com.cts.project.Travelling_package.Repository.ItineraryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class ItineraryService {
//
//    @Autowired
//    private ItineraryRepository itineraryRepository;
//
//    public Itinerary createItinerary(Itinerary itinerary) {
//        return itineraryRepository.save(itinerary);
//    }
//
//    public Itinerary addActivity(Long itineraryId, Activity activity) {
//        Itinerary itinerary = itineraryRepository.findById(itineraryId).orElseThrow(() -> new RuntimeException("Itinerary not found"));
//        itinerary.getActivities().add(activity);
//        return itineraryRepository.save(itinerary);
//    }
//
//    public Itinerary removeActivity(Long itineraryId, Long activityId) {
//        Itinerary itinerary = itineraryRepository.findById(itineraryId).orElseThrow(() -> new RuntimeException("Itinerary not found"));
//        itinerary.getActivities().removeIf(activity -> activity.getActivityId().equals(activityId));
//        return itineraryRepository.save(itinerary);
//    }
//
//    public List<Itinerary> getAllItineraries() {
//        return itineraryRepository.findAll();
//    }
//
//    public Itinerary getItineraryById(Long itineraryId) {
//        return itineraryRepository.findById(itineraryId).orElse(null);
//    }
//
//    public void deleteItinerary(Long itineraryId) {
//        itineraryRepository.deleteById(itineraryId);
//    }
//}
package com.cts.project.Travelling_package.Service;

import com.cts.project.Travelling_package.Dto.ItineraryResponse;
import com.cts.project.Travelling_package.Model.*;
import com.cts.project.Travelling_package.Model.Package;
import com.cts.project.Travelling_package.PaymentDto.PaymentRequest;
import com.cts.project.Travelling_package.PaymentDto.PaymentResponse;
import com.cts.project.Travelling_package.Repository.ActivityRepository;
import com.cts.project.Travelling_package.Repository.ItineraryRepository;
import com.cts.project.Travelling_package.Repository.PackageRepository;
import com.cts.project.Travelling_package.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ItineraryService {

    @Autowired
    private ItineraryRepository itineraryRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private PaymentService paymentService;

    @Transactional
    public Itinerary createItinerary(int userId, Long packageId) {
        Optional<Package> packageOpt = packageRepository.findById(packageId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (packageOpt.isPresent() && userOpt.isPresent()) {
            Itinerary itinerary = new Itinerary();
            itinerary.setUser(userOpt.get());
            itinerary.setTravelPackage(packageOpt.get());

            // Create new sets instead of referencing existing collections
//            List<Hotel> hotels = new ArrayList<>(packageOpt.get().getIncludedHotels());
//            List<Flight> flights = new ArrayList<>(packageOpt.get().getIncludedFlights());
//            List<Package.Activity> activities = new ArrayList<>(packageOpt.get().getActivities());

//            itinerary.setIncludedHotels(hotels);
//            itinerary.setIncludedFlights(flights);
//            itinerary.setActivities(activities);

            itinerary.updateTotalPrice();  // Auto calculate total price

            return itineraryRepository.save(itinerary);
        }


        return null;
    }

    @Transactional
    public Itinerary addActivity(Long itineraryId, Long activityId) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));

        Optional<Package.Activity> activityOpt = activityRepository.findById(activityId);
        if (activityOpt.isEmpty()) {
            throw new RuntimeException("Activity not found");
        }

        itinerary.getCustomizedActivities().add(activityOpt.get());
        itinerary.updateTotalPrice(); // Update price dynamically
        return itineraryRepository.save(itinerary);
    }

    @Transactional
    public Itinerary removeActivity(Long itineraryId, Long activityId) {
        Itinerary itinerary = itineraryRepository.findById(itineraryId)
                .orElseThrow(() -> new RuntimeException("Itinerary not found"));

        itinerary.getCustomizedActivities().removeIf(activity -> activity.getActivityId().equals(activityId));
        itinerary.updateTotalPrice(); // Update price dynamically
        return itineraryRepository.save(itinerary);
    }








}