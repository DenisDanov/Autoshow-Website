package com.example.app.services.impl;

import com.example.app.data.entities.User;
import com.example.app.services.RegistrationControllerService;
import com.example.app.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class RegistrationControllerServiceImpl implements RegistrationControllerService {

    private final UserService userService;

    public RegistrationControllerServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String processRegistration(User user) {
        User userFromDB = userService.findByUsername(user.getUsername());
        String emailRegex = "^[A-Za-z0-9]+[-._A-Za-z0-9]{0,}[A-Za-z0-9.-_]{0,}[@]{1}[A-Za-z-_]+[.]{1}[A-Za-z-_.]+[A-Za-z.-_]{0,}$";

        if (userFromDB == null && userService.findByEmail(user.getEmail()) == null) {
            if (user.getUsername().isBlank() || user.getPassword().isBlank()) {
                return "redirect:/register?errorBlank";
            }

            if (user.getPassword().length() < 8) {
                return "redirect:/register?errorPassword";
            }

            // Validate email using regex
            if (!user.getEmail().matches(emailRegex)) {
                return "redirect:/register?errorEmail";
            }

            // Save the user to the database
            User savedUser = userService.save(user);

            // Check if the user is saved successfully
            if (savedUser != null && savedUser.getId() != null) {
                // Log a success message or return a success view
                System.out.println("User saved successfully. ID: " + savedUser.getId());
                return "redirect:/login";
            } else {
                System.err.println("Error saving user to the database");
                return "redirect:/register?error";
            }
        } else {
            // Log an error message or return an error view
            System.err.println("Error saving user to the database");
            if (userFromDB != null) {
                return "redirect:/register?error";
            } else {
                return "redirect:/register?errorEmailExists";
            }
        }
    }
}
