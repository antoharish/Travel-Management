package com.cts.project.Travelling_package.Model;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ReviewID")
    private int reviewID;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "hotelId", nullable = false)
    private Hotel hotel;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "userId", nullable = false)
    private User user;


    @Column(name = "Rating", nullable = false)
    private double rating;

    @Column(name = "Comment", nullable = false)
    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Timestamp", nullable = false, updatable = false)
    private Date timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = new Date();
    }
}