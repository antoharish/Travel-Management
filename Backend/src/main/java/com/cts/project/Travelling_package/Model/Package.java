package com.cts.project.Travelling_package.Model;
import java.time.LocalDate;
import java.util.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Package")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long PackageId;
    private String Name;
    private double Price;
    private int noOfDays;
    private int noOfPeople;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "packages_created", nullable = false)
    private int packagesCreated;





    @ManyToMany
    @JoinTable(
            name = "package_hotels",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "hotelId")
    )
    private List<Hotel> includedHotels;

    @ManyToMany
    @JoinTable(
            name = "package_flights",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "flightId")
    )
    private List<Flight> includedFlights;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "package_id")
    private List<Activity> activities;


    @Column(name = "total_price", nullable = false)
    private double PackagetotalPrice;

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }


    @Entity
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Table(name = "Activity")
    public static class Activity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long activityId;
        private String name;
        private String description;
        private Double price;
        private String location;
    }

}