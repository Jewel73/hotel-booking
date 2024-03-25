package edu.jewel.hotelbookingapp.service;

import edu.jewel.hotelbookingapp.model.HotelManager;
import edu.jewel.hotelbookingapp.model.User;

public interface HotelManagerService {

    HotelManager findByUser(User user);

}
