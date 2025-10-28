package com.cts.project.Travelling_package.Controller;

import com.cts.project.Travelling_package.Exception.ResourceNotFoundException;
import com.cts.project.Travelling_package.Model.Hotel;
import com.cts.project.Travelling_package.Model.Review;
import com.cts.project.Travelling_package.Model.User;
import com.cts.project.Travelling_package.Repository.HotelRepository;
import com.cts.project.Travelling_package.Repository.ReviewRepository;
import com.cts.project.Travelling_package.Repository.UserRepository;
import com.cts.project.Travelling_package.Service.ReviewService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/review")
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable int id) {
        Optional<Review> review = reviewService.getReviewById(id);
        return review.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/hotel/{hotelId}")
    public List<Review> getReviewsByHotelId(@PathVariable int hotelId) {
        return reviewService.getReviewsByHotelId(hotelId);
    }

    @GetMapping("/user/{userId}")
    public List<Review> getReviewsByUserId(@PathVariable int userId) { // Updated method
        return reviewService.getReviewsByUserId(userId);
    }
    @GetMapping("/rating/above/{rating}")
    public List<Review> getReviewsWithRatingAbove(@PathVariable double rating) {
        return reviewService.getReviewsWithRatingAbove(rating);
    }


    @PostMapping("/user/postReview")
    public Review addReview(@RequestBody Review review) {

        if (review.getHotel() == null || review.getHotel().getHotelId() <= 0) {
            throw new IllegalArgumentException("Hotel information is missing or invalid");
        }
        if (review.getUser() == null || review.getUser().getUserId() <= 0) {
            throw new IllegalArgumentException("User information is missing or invalid");
        }

        Hotel managedHotel = hotelRepository.findById(review.getHotel().getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found"));

        User managedUser = userRepository.findById(review.getUser().getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        review.setHotel(managedHotel);
        review.setUser(managedUser);
        review.setTimestamp(new Date());
        return reviewRepository.save(review);
    }



    @PutMapping("/user/update/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable int id, @RequestBody Review reviewDetails) {
        Review updatedReview = reviewService.updateReview(id, reviewDetails);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/user/delete/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable int id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/hotel/{hotelId}/average-rating")
    public Double getAverageRating(@PathVariable Long hotelId) {
        return reviewService.getAverageRatingByHotelId(hotelId);
    }
}
