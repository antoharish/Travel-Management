package com.cts.project.Travelling_package.Repository;

import com.cts.project.Travelling_package.Model.HotelAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface HotelAvailabilityRepository extends JpaRepository<HotelAvailability, Long> {

    // Find available hotels by location, date, and minimum capacity
    List<HotelAvailability> findByHotel_LocationAndDateAndCapacityGreaterThan(
            String location, LocalDate date, Integer capacity
    );


    Optional<HotelAvailability> findByHotel_HotelIdAndDate(Long hotelId, LocalDate date);


    List<HotelAvailability> findByDateBefore(LocalDate date);

    @Query("SELECT h FROM HotelAvailability h WHERE h.hotel.id = :hotelId AND h.date = :date")
    Optional<HotelAvailability> findByHotelIdAndDate(@Param("hotelId") Long hotelId, @Param("date") LocalDate date);

//    Optional<HotelAvailability> findByHotelIdAndDate(Long hotelId, LocalDate date);


    public abstract List<HotelAvailability> findByLocationAndDateAndCapacityGreaterThan(String location, LocalDate date, int capacity);
}
