package com.cts.project.Travelling_package.Service;

import com.cts.project.Travelling_package.Model.Hotel;
import com.cts.project.Travelling_package.Model.Package;
import com.cts.project.Travelling_package.Repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public List<Package.Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    public Package.Activity getActivityById(Long activityId) {
        return activityRepository.findById(activityId).orElse(null);
    }

    public Package.Activity createActivity(Package.Activity activity) {
        return activityRepository.save(activity);
    }

    public void deleteActivity(Long activityId) {
        activityRepository.deleteById(activityId);
    }


    public Package.Activity updateActivity(Long id, Package.Activity updatedActivity) {
        Optional<Package.Activity> existingActivity = activityRepository.findById(id);
        if (existingActivity.isPresent()) {
            Package.Activity activity = existingActivity.get();
            activity.setName(updatedActivity.getName());
            activity.setDescription(updatedActivity.getDescription());
            activity.setPrice(updatedActivity.getPrice());
            return activityRepository.save(activity);
        } else {
            return null;
        }
    }
    public List<Package.Activity> getActivityByLocation(String location){
        return activityRepository.findByLocation(location);
    }
}