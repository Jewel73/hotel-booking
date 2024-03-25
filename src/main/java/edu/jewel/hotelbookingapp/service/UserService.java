package edu.jewel.hotelbookingapp.service;

import edu.jewel.hotelbookingapp.model.User;
import edu.jewel.hotelbookingapp.model.dto.ResetPasswordDTO;
import edu.jewel.hotelbookingapp.model.dto.UserDTO;
import edu.jewel.hotelbookingapp.model.dto.UserRegistrationDTO;

import java.util.List;

public interface UserService {

    User saveUser(UserRegistrationDTO registrationDTO);

    // For registration
    User findUserByUsername(String username);

    UserDTO findUserDTOByUsername(String username);

    UserDTO findUserById(Long id);

    List<UserDTO> findAllUsers();

    void updateUser(UserDTO userDTO);

    void updateLoggedInUser(UserDTO userDTO);

    void deleteUserById(Long id);

    User resetPassword(ResetPasswordDTO resetPasswordDTO);

}
