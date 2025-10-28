//package com.cts.project.Travelling_package.Model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "itinerary")
//public class Itinerary {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long itineraryId;
//
//    @Column(name = "package_id", nullable = false)
//    private Long packageId;
//
//    @Column(name = "user_id", nullable = false)
//    private Long userId;
//
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "package_id")
//    private List<Package.Activity> activities;
//
//    // Getter for activities
//    public List<Package.Activity> getActivities() {
//        return activities;
//    }
//
//    // Setter for activities
//    public void setActivities(List<Package.Activity> activities) {
//        this.activities = activities;
//    }
//}
package com.cts.project.Travelling_package.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "itinerary")
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itinerary_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // The user who owns this itinerary

    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    private Package travelPackage;  // The package selected by the user

    @ManyToMany
    @JoinTable(
            name = "itinerary_hotels",
            joinColumns = @JoinColumn(name = "itinerary_id"),
            inverseJoinColumns = @JoinColumn(name = "hotel_id")
    )
    private List<Hotel> includedHotels = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "itinerary_flights",
            joinColumns = @JoinColumn(name = "itinerary_id"),
            inverseJoinColumns = @JoinColumn(name = "flight_id")
    )
    private List<Flight> includedFlights = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "itinerary_activities",
            joinColumns = @JoinColumn(name = "itinerary_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id")
    )
    private List<Package.Activity> activities = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "itinerary_id")
    private List<Hotel> customizedHotels = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "itinerary_id")
    private List<Flight> customizedFlights = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "itinerary_id")
    private List<Package.Activity> customizedActivities = new ArrayList<>();

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    public void updateTotalPrice() {
        double price = travelPackage.getPackagetotalPrice(); // Start with base package price

        for (Hotel hotel : customizedHotels) {
            price += hotel.getPricePerNight();
        }

        for (Flight flight : customizedFlights) {
            price += flight.getPrice();
        }

        for (Package.Activity activity : customizedActivities) {
            price += activity.getPrice();
        }

        this.totalPrice = price; // Update total price dynamically
    }
    public double getTotalPrice() {
        return totalPrice;
    }

    public List<Package.Activity> getActivities() {
        return activities;
    }

}