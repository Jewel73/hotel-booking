package edu.jewel.hotelbookingapp.model.dto;

import lombok.Data;

@Data
public class ReviewDTO {
    private Long customerId;
    private Long hotelId;
    private String comment;
    private int rating;
}