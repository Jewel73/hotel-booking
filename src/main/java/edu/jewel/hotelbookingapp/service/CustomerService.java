package edu.jewel.hotelbookingapp.service;

import edu.jewel.hotelbookingapp.model.Customer;

import java.util.Optional;

public interface CustomerService {

    Optional<Customer> findByUserId(Long userId);

    Optional<Customer> findByUsername(String username);
}
