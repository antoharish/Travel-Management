package com.cts.project.Travelling_package.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class HotelAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private LocalDate date;

    private Integer capacity;

    @ManyToOne ()
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;  // Link to the Hotel entity


//    @Transient
    private String location;


    public String getHotelLocation() {
        if (this.hotel != null) {
            return this.hotel.getLocation(); // Assuming Hotel has a getLocation() method
        }
        return null;
    }
}
