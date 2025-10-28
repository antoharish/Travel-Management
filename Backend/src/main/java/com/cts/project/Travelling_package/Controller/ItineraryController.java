package com.cts.project.Travelling_package.Controller;//package com.cts.project.Travelling_package.Controller;
//
//import com.cts.project.Travelling_package.Model.Package;

import com.cts.project.Travelling_package.Dto.ItineraryRequest;
import com.cts.project.Travelling_package.Service.ItineraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.cts.project.Travelling_package.Model.Itinerary;

////import com.cts.project.Travelling_package.Model.Itinerary;
////import com.cts.project.Travelling_package.Service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itineraries")
public class ItineraryController {

    @Autowired
    private ItineraryService itineraryService;

    //
//    @PostMapping
//    public Itinerary createItinerary(@RequestBody Itinerary itinerary) {
//        return itineraryService.createItinerary(itinerary);
//    }
//
//    @PostMapping("/{id}/activities/{activityId}")
//    public Itinerary addActivity(@PathVariable Long id, @PathVariable Package.Activity activityId) {
//        return itineraryService.addActivity(id, activityId);
//    }
//
//    @DeleteMapping("/{id}/activities/{activityId}")
//    public Itinerary removeActivity(@PathVariable Long id, @PathVariable Long activityId) {
//        return itineraryService.removeActivity(id, activityId);
//    }
//
//    @GetMapping
//    public List<Itinerary> getAllItineraries() {
//        return itineraryService.getAllItineraries();
//    }
//
//    @GetMapping("/{id}")
//    public Itinerary getItineraryById(@PathVariable Long id) {
//        return itineraryService.getItineraryById(id);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteItinerary(@PathVariable Long id) {
//        itineraryService.deleteItinerary(id);
//    }
//}
    @PostMapping
    public ResponseEntity<Itinerary> createItinerary(@RequestBody ItineraryRequest request) {
        Itinerary itinerary = itineraryService.createItinerary(request.getUserId(), request.getPackageId());
        return ResponseEntity.status(HttpStatus.CREATED).body(itinerary);
    }

    @PostMapping("/{itineraryId}/addActivity")
    public ResponseEntity<Itinerary> addActivity(@PathVariable Long itineraryId, @RequestParam Long activityId) {
        Itinerary updatedItinerary = itineraryService.addActivity(itineraryId, activityId);
        return ResponseEntity.ok(updatedItinerary);
    }

    @DeleteMapping("/{itineraryId}/removeActivity")
    public ResponseEntity<Itinerary> removeActivity(@PathVariable Long itineraryId, @RequestParam Long activityId) {
        Itinerary updatedItinerary = itineraryService.removeActivity(itineraryId, activityId);
        return ResponseEntity.ok(updatedItinerary);
    }
}
