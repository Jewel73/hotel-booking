package edu.jewel.hotelbookingapp.service;

import edu.jewel.hotelbookingapp.model.Booking;
import edu.jewel.hotelbookingapp.model.dto.BookingDTO;
import edu.jewel.hotelbookingapp.model.dto.BookingInitiationDTO;

import java.util.List;

public interface BookingService {

    Booking saveBooking(BookingInitiationDTO bookingInitiationDTO, Long customerId);

    BookingDTO confirmBooking(BookingInitiationDTO bookingInitiationDTO, Long customerId);

    List<BookingDTO> findAllBookings();

    BookingDTO findBookingById(Long bookingId);

    List<BookingDTO> findBookingsByCustomerId(Long customerId);

    BookingDTO findBookingByIdAndCustomerId(Long bookingId, Long customerId);

    List<BookingDTO> findBookingsByManagerId(Long managerId);

    BookingDTO findBookingByIdAndManagerId(Long bookingId, Long managerId);

    BookingDTO mapBookingModelToBookingDto(Booking booking);

    List<Booking> findByCustomerAndHotelId(Long customerId, Long hotelId);

}
