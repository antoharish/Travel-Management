package com.cts.project.Travelling_package.Service;

import com.cts.project.Travelling_package.Exception.ResourceNotFoundException;
import com.cts.project.Travelling_package.Model.Review;
import com.cts.project.Travelling_package.Repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Optional<Review> getReviewById(int reviewID) {
        return reviewRepository.findById(reviewID);
    }

    public List<Review> getReviewsByHotelId(int hotelId) {
        return reviewRepository.findByHotel_HotelId(hotelId);
    }

    public List<Review> getReviewsByUserId(int userID) {
        return reviewRepository.findByUser_UserId(userID);
    }

    public List<Review> getReviewsWithRatingAbove(double rating) {
        return reviewRepository.findByRatingGreaterThan(rating);
    }

    public List<Review> searchReviewsByKeyword(String keyword) {
        return reviewRepository.findByCommentContaining(keyword);
    }

    public Review addReview(Review review) {
        review.setTimestamp(new Date());
        return reviewRepository.save(review);
    }

    public Review updateReview(int reviewID, Review reviewDetails) {
        Review review = reviewRepository.findById(reviewID)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        review.setRating(reviewDetails.getRating());
        review.setComment(reviewDetails.getComment());
        review.setTimestamp(new Date());
        return reviewRepository.save(review);
    }

    public void deleteReview(int reviewID) {
        Review review = reviewRepository.findById(reviewID)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        reviewRepository.delete(review);
    }
    public Double getAverageRatingByHotelId(Long hotelId) {
        Double avg = reviewRepository.findAverageRatingByHotelId(hotelId);
        return avg != null ? avg : 0.0;
    }
}
