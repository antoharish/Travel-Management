package com.cts.project.Travelling_package.Controller;


import com.cts.project.Travelling_package.Model.Hotel;
import com.cts.project.Travelling_package.Model.Package;
import com.cts.project.Travelling_package.Repository.ActivityRepository;
import com.cts.project.Travelling_package.Service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activities")
public class ActivityController {
    private ActivityRepository activityRepository;


    @Autowired
    private ActivityService activityService;

    @PostMapping
    public ResponseEntity<Package.Activity> createActivity(@RequestBody Package.Activity activity) {
        Package.Activity createdActivity = activityService.createActivity(activity);
        return new ResponseEntity<>(createdActivity, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Package.Activity>> getAllActivities() {
        List<Package.Activity> activities = activityService.getAllActivities();
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public void deleteActivity(@PathVariable Long id) {
        activityService.deleteActivity(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Package.Activity> updateActivity(
            @PathVariable Long id,
            @RequestBody Package.Activity updatedActivity) {
        Package.Activity activity = activityService.updateActivity(id, updatedActivity);
        if (activity != null) {
            return ResponseEntity.ok(activity);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/GetActivityByLocation")
    public ResponseEntity<List<Package.Activity>> getActivity(
            @RequestParam String location
    ){
        List<Package.Activity> getActivity = activityService.getActivityByLocation(location);
        return new ResponseEntity<>(getActivity, HttpStatus.OK);
    }

//    PutMapping("/{id}")
//    public ResponseEntity<Package.Activity> updateHotel(@PathVariable Long id, @RequestBody Hotel updatedHotel) {
//        Package.Activity Activity = activityService.updateActivity(id, updatedHotel);
//        if (Activity != null) {
//            return ResponseEntity.ok(Activity);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
