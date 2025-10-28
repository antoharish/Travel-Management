package com.cts.project.Travelling_package.Repository;

import com.cts.project.Travelling_package.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByHotel_HotelId(int hotelId);

    List<Review> findByUser_UserId(int userId); // Corrected method

    List<Review> findByRatingGreaterThan(double rating);

    List<Review> findByCommentContaining(String keyword);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.hotel.hotelId = :hotelId")
    Double findAverageRatingByHotelId(@Param("hotelId") Long hotelId);
}