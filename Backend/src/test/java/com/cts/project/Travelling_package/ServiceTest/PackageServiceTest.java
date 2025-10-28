package com.cts.project.Travelling_package.ServiceTest;

import com.cts.project.Travelling_package.Model.*;
import com.cts.project.Travelling_package.Model.Package;
import com.cts.project.Travelling_package.Repository.*;
import com.cts.project.Travelling_package.Service.PackageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PackageServiceTest {

    @Mock private PackageRepository packageRepository;
    @Mock private ActivityRepository activityRepository;
    @Mock private FlightRepository flightRepository;
    @Mock private HotelRepository hotelRepository;
    @Mock private FlightAvailabilityRepository flightAvailabilityRepository;
    @Mock private HotelAvailabilityRepository hotelAvailabilityRepository;

    @InjectMocks private PackageService packageService;

    private Package travelPackage;

    @BeforeEach
    void setUp() {
        travelPackage = new Package();
        travelPackage.setPackageId(1L);
        travelPackage.setName("Test Package");
        travelPackage.setPrice(1000.0);
        travelPackage.setNoOfPeople(2);
        travelPackage.setNoOfDays(3);
        travelPackage.setLocation("Paris");
        travelPackage.setStartDate(LocalDate.now());
        travelPackage.setEndDate(LocalDate.now().plusDays(3));
        travelPackage.setPackagesCreated(1);
        travelPackage.setIncludedHotels(new ArrayList<>());
        travelPackage.setIncludedFlights(new ArrayList<>());
        travelPackage.setActivities(new ArrayList<>());
        travelPackage.setPackagetotalPrice(1000.0);
    }

    @Test
    void testCreatePackage_Success() {
        when(packageRepository.save(any(Package.class))).thenReturn(travelPackage);

        Package result = packageService.createPackage(travelPackage);

        assertNotNull(result);
        verify(packageRepository, times(2)).save(any(Package.class));
    }

    @Test
    void testGetAllPackages() {
        when(packageRepository.findAll()).thenReturn(List.of(travelPackage));
        List<Package> result = packageService.getAllPackages();
        assertEquals(1, result.size());
    }

    @Test
    void testGetPackageById_Found() {
        when(packageRepository.findById(1L)).thenReturn(Optional.of(travelPackage));
        Package result = packageService.getPackageById(1L);
        assertNotNull(result);
    }

    @Test
    void testGetPackageById_NotFound() {
        when(packageRepository.findById(2L)).thenReturn(Optional.empty());
        Package result = packageService.getPackageById(2L);
        assertNull(result);
    }

    @Test
    void testAddActivityToPackage_Success() {
        Package.Activity activity = new Package.Activity(1L, "A", "desc", 100.0, "Paris");
        when(packageRepository.findById(1L)).thenReturn(Optional.of(travelPackage));
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(packageRepository.save(any(Package.class))).thenReturn(travelPackage);

        Package result = packageService.addActivityToPackage(1L, 1L);
        assertNotNull(result);
        verify(packageRepository).save(travelPackage);
    }

    @Test
    void testRemoveActivityFromPackage_Success() {
        Package.Activity activity = new Package.Activity(1L, "A", "desc", 100.0, "Paris");
        travelPackage.getActivities().add(activity);
        when(packageRepository.findById(1L)).thenReturn(Optional.of(travelPackage));
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));
        when(packageRepository.save(any(Package.class))).thenReturn(travelPackage);

        Package result = packageService.removeActivityFromPackage(1L, 1L);
        assertNotNull(result);
        verify(packageRepository).save(travelPackage);
    }

    @Test
    void testDeletePackage() {
        doNothing().when(packageRepository).deleteById(1L);
        packageService.deletePackage(1L);
        verify(packageRepository).deleteById(1L);
    }

    @Test
    void testUpdatePackage_Success() {
        Package updated = new Package();
        updated.setName("Updated");
        updated.setPrice(2000.0);
        updated.setNoOfDays(5);
        updated.setNoOfPeople(4);
        updated.setLocation("London");
        updated.setStartDate(LocalDate.now());
        updated.setEndDate(LocalDate.now().plusDays(5));
        updated.setPackagesCreated(2);
        updated.setIncludedHotels(new ArrayList<>());
        updated.setIncludedFlights(new ArrayList<>());
        updated.setActivities(new ArrayList<>());
        updated.setPackagetotalPrice(2000.0);

        when(packageRepository.findById(1L)).thenReturn(Optional.of(travelPackage));
        when(packageRepository.save(any(Package.class))).thenReturn(updated);

        Package result = packageService.updatePackage(1L, updated);
        assertEquals("Updated", result.getName());
        assertEquals(2000.0, result.getPrice());
    }
}
