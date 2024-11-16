package edu.jewel.hotelbookingapp.repository;

import edu.jewel.hotelbookingapp.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByHotelId(Long hotelId);

    Optional<Review> findByCustomerIdAndHotelId(Long customerId, Long hotelId);
}
