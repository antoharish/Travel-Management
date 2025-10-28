package com.cts.project.Travelling_package.ServiceTest;

import com.cts.project.Travelling_package.Exception.ResourceNotFoundException;
import com.cts.project.Travelling_package.Model.Review;
import com.cts.project.Travelling_package.Repository.ReviewRepository;
import com.cts.project.Travelling_package.Service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review review;

    @BeforeEach
    void setUp() {
        review = new Review();
        review.setReviewID(1);
        review.setRating(4.5);
        review.setComment("Great stay!");
    }

    @Test
    void testGetAllReviews() {
        List<Review> reviews = List.of(review);
        when(reviewRepository.findAll()).thenReturn(reviews);

        List<Review> result = reviewService.getAllReviews();
        assertEquals(1, result.size());
        assertEquals(review, result.get(0));
    }

    @Test
    void testGetReviewById() {
        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));
        Optional<Review> result = reviewService.getReviewById(1);
        assertTrue(result.isPresent());
        assertEquals(review, result.get());
    }

    @Test
    void testGetReviewsByHotelId() {
        when(reviewRepository.findByHotel_HotelId(10)).thenReturn(List.of(review));
        List<Review> result = reviewService.getReviewsByHotelId(10);
        assertEquals(1, result.size());
    }

    @Test
    void testGetReviewsByUserId() {
        when(reviewRepository.findByUser_UserId(5)).thenReturn(List.of(review));
        List<Review> result = reviewService.getReviewsByUserId(5);
        assertEquals(1, result.size());
    }

    @Test
    void testGetReviewsWithRatingAbove() {
        when(reviewRepository.findByRatingGreaterThan(4.0)).thenReturn(List.of(review));
        List<Review> result = reviewService.getReviewsWithRatingAbove(4.0);
        assertEquals(1, result.size());
    }

    @Test
    void testSearchReviewsByKeyword() {
        when(reviewRepository.findByCommentContaining("Great")).thenReturn(List.of(review));
        List<Review> result = reviewService.searchReviewsByKeyword("Great");
        assertEquals(1, result.size());
    }

    @Test
    void testAddReview() {
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        Review result = reviewService.addReview(review);
        assertNotNull(result.getTimestamp());
        assertEquals(review, result);
    }

    @Test
    void testUpdateReview_Success() {
        Review updated = new Review();
        updated.setRating(5.0);
        updated.setComment("Excellent!");

        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review result = reviewService.updateReview(1, updated);
        assertEquals(5.0, result.getRating());
        assertEquals("Excellent!", result.getComment());
        assertNotNull(result.getTimestamp());
    }

    @Test
    void testUpdateReview_NotFound() {
        when(reviewRepository.findById(2)).thenReturn(Optional.empty());
        Review updated = new Review();
        assertThrows(ResourceNotFoundException.class, () -> reviewService.updateReview(2, updated));
    }

    @Test
    void testDeleteReview_Success() {
        when(reviewRepository.findById(1)).thenReturn(Optional.of(review));
        doNothing().when(reviewRepository).delete(review);
        assertDoesNotThrow(() -> reviewService.deleteReview(1));
        verify(reviewRepository).delete(review);
    }

    @Test
    void testDeleteReview_NotFound() {
        when(reviewRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reviewService.deleteReview(2));
    }

    @Test
    void testGetAverageRatingByHotelId() {
        when(reviewRepository.findAverageRatingByHotelId(10L)).thenReturn(4.2);
        Double avg = reviewService.getAverageRatingByHotelId(10L);
        assertEquals(4.2, avg);
    }

    @Test
    void testGetAverageRatingByHotelId_Null() {
        when(reviewRepository.findAverageRatingByHotelId(10L)).thenReturn(null);
        Double avg = reviewService.getAverageRatingByHotelId(10L);
        assertEquals(0.0, avg);
    }
}
