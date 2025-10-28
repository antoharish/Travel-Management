package com.cts.project.Travelling_package.ServiceTest;

import com.cts.project.Travelling_package.Model.Package;
import com.cts.project.Travelling_package.Repository.ActivityRepository;
import com.cts.project.Travelling_package.Service.ActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock private ActivityRepository activityRepository;
    @InjectMocks private ActivityService activityService;

    private Package.Activity activity;

    @BeforeEach
    void setUp() {
        activity = new Package.Activity(1L, "Hiking", "Mountain hike", 50.0, "Alps");
    }

    @Test
    void testGetAllActivities() {
        when(activityRepository.findAll()).thenReturn(List.of(activity));
        List<Package.Activity> result = activityService.getAllActivities();
        assertEquals(1, result.size());
        assertEquals(activity, result.get(0));
    }

    @Test
    void testGetActivityById_Found() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        Package.Activity result = activityService.getActivityById(1L);
        assertNotNull(result);
        assertEquals(activity, result);
    }

    @Test
    void testGetActivityById_NotFound() {
        when(activityRepository.findById(2L)).thenReturn(Optional.empty());
        Package.Activity result = activityService.getActivityById(2L);
        assertNull(result);
    }

    @Test
    void testCreateActivity() {
        when(activityRepository.save(activity)).thenReturn(activity);
        Package.Activity result = activityService.createActivity(activity);
        assertEquals(activity, result);
    }

    @Test
    void testDeleteActivity() {
        doNothing().when(activityRepository).deleteById(1L);
        activityService.deleteActivity(1L);
        verify(activityRepository).deleteById(1L);
    }

    @Test
    void testUpdateActivity_Found() {
        Package.Activity updated = new Package.Activity(1L, "Rafting", "River rafting", 80.0, "Amazon");
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(activityRepository.save(any(Package.Activity.class))).thenReturn(updated);

        Package.Activity result = activityService.updateActivity(1L, updated);
        assertNotNull(result);
        assertEquals("Rafting", activity.getName());
        assertEquals("River rafting", activity.getDescription());
        assertEquals(80.0, activity.getPrice());
    }

    @Test
    void testUpdateActivity_NotFound() {
        Package.Activity updated = new Package.Activity(2L, "Rafting", "River rafting", 80.0, "Amazon");
        when(activityRepository.findById(2L)).thenReturn(Optional.empty());
        Package.Activity result = activityService.updateActivity(2L, updated);
        assertNull(result);
    }

    @Test
    void testGetActivityByLocation() {
        when(activityRepository.findByLocation("Alps")).thenReturn(List.of(activity));
        List<Package.Activity> result = activityService.getActivityByLocation("Alps");
        assertEquals(1, result.size());
        assertEquals(activity, result.get(0));
    }
}
