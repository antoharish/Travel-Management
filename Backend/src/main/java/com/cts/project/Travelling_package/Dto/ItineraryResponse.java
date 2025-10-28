package com.cts.project.Travelling_package.Dto;

import com.cts.project.Travelling_package.Model.*;
import com.cts.project.Travelling_package.Model.Package;
import java.util.List;


public class ItineraryResponse {

    private Long itineraryId;
    private User user;
    private Package travelPackage;
    private List<Hotel> customizedHotels;
    private List<Flight> customizedFlights;
    private List<Package.Activity> customizedActivities;
    private double totalPrice;

    public ItineraryResponse(Long itineraryId, User user, Package travelPackage,
                             List<Hotel> customizedHotels, List<Flight> customizedFlights,
                             List<Package.Activity> customizedActivities, double totalPrice) {
        this.itineraryId = itineraryId;
        this.user = user;
        this.travelPackage = travelPackage;
        this.customizedHotels = customizedHotels;
        this.customizedFlights = customizedFlights;
        this.customizedActivities = customizedActivities;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters for every field
    public Long getItineraryId() { return itineraryId; }
    public void setItineraryId(Long itineraryId) { this.itineraryId = itineraryId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Package getTravelPackage() { return travelPackage; }
    public void setTravelPackage(Package travelPackage) { this.travelPackage = travelPackage; }

    public List<Hotel> getCustomizedHotels() { return customizedHotels; }
    public void setCustomizedHotels(List<Hotel> customizedHotels) { this.customizedHotels = customizedHotels; }

    public List<Flight> getCustomizedFlights() { return customizedFlights; }
    public void setCustomizedFlights(List<Flight> customizedFlights) { this.customizedFlights = customizedFlights; }

    public List<Package.Activity> getCustomizedActivities() { return customizedActivities; }
    public void setCustomizedActivities(List<Package.Activity> customizedActivities) { this.customizedActivities = customizedActivities; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
