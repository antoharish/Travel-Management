package com.cts.project.Travelling_package.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.cts.project.Travelling_package.Model.Review;
import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "hotel")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private Long hotelId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "roomsAvailable", nullable = false)
    private int roomsAvailable;

    @Column(name = "rating", nullable = false)
    private double rating;

    @Column(name = "pricePerNight", nullable = false)
    private double pricePerNight;


    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    @Transient
    @OneToMany(mappedBy = "hotel" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List <HotelAvailability> availabilityList;


}
