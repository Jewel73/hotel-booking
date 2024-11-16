package edu.jewel.hotelbookingapp.service;

import edu.jewel.hotelbookingapp.model.Review;

import java.util.List;

public interface ReviewService {
    /**
     * Adds a new review for a hotel.
     *
     * @param review the review to add
     * @return the added review
     */
    Review addReview(Review review);

    /**
     * Fetches all reviews for a specific hotel.
     *
     * @param hotelId the ID of the hotel
     * @return a list of reviews for the hotel
     */
    List<Review> getReviewsByHotel(Long hotelId);

    /**
     * Deletes a review by its ID.
     *
     * @param reviewId the ID of the review to delete
     */
    void deleteReview(Long reviewId);
}
