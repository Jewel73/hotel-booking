package edu.jewel.hotelbookingapp.service.impl;

import edu.jewel.hotelbookingapp.model.Review;
import edu.jewel.hotelbookingapp.repository.ReviewRepository;
import edu.jewel.hotelbookingapp.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review addReview(Review review) {
        // Add business logic if needed (e.g., prevent duplicate reviews by a user for the same hotel)
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsByHotel(Long hotelId) {
        return reviewRepository.findByHotelId(hotelId);
    }

    @Override
    public void deleteReview(Long reviewId) {
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
        } else {
            throw new IllegalArgumentException("Review with ID " + reviewId + " does not exist.");
        }
    }

    public Optional<Review> getReviewByCustomerAndHotel(Long customerId, Long hotelId) {
        return reviewRepository.findByCustomerIdAndHotelId(customerId, hotelId);
    }
}
