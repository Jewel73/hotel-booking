package edu.jewel.hotelbookingapp.controller;

import edu.jewel.hotelbookingapp.model.Customer;
import edu.jewel.hotelbookingapp.model.Hotel;
import edu.jewel.hotelbookingapp.model.Review;
import edu.jewel.hotelbookingapp.model.dto.ReviewDTO;
import edu.jewel.hotelbookingapp.service.BookingService;
import edu.jewel.hotelbookingapp.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private BookingService bookingService;

    @PostMapping("/add")
    public ResponseEntity<String> submitReview(@ModelAttribute ReviewDTO reviewDto, @RequestHeader(HttpHeaders.REFERER) String referer) {
        // Check if the customer has booked the hotel
//        boolean hasBooking = bookingService.getBookingByCustomerAndHotel(reviewDto.getCustomerId(), reviewDto.getHotelId())
//                .isPresent();
//        if (!hasBooking) {
//            return ResponseEntity.badRequest().body("You cannot submit a review for a hotel you haven't booked.");
//        }

        // Check if a review already exists
        boolean alreadyReviewed = reviewService.getReviewByCustomerAndHotel(reviewDto.getCustomerId(), reviewDto.getHotelId())
                .isPresent();
        if (alreadyReviewed) {
            return ResponseEntity.badRequest().body("You have already submitted a review for this hotel.");
        }

        // Save the review
        Review review = Review.builder()
                .customer(Customer.builder().id(reviewDto.getCustomerId()).build())
                .hotel(Hotel.builder().id(reviewDto.getHotelId()).build())
                .comment(reviewDto.getComment())
                .rating(reviewDto.getRating())
                .build();

        reviewService.addReview(review);

        return ResponseEntity.status(302).header("Location", referer).build();
    }
}
