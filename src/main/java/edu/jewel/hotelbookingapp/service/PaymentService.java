package edu.jewel.hotelbookingapp.service;

import edu.jewel.hotelbookingapp.model.Booking;
import edu.jewel.hotelbookingapp.model.Payment;
import edu.jewel.hotelbookingapp.model.dto.BookingInitiationDTO;

public interface PaymentService {

    Payment savePayment(BookingInitiationDTO bookingInitiationDTO, Booking booking);
}
