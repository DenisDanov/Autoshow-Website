package com.example.demo.dbData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final UserRepository userRepository;

    @Autowired
    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") User user) {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if (userFromDB == null) {

            // Save the user to the database
            User savedUser = userRepository.save(user);
            // Check if the user is saved successfully
            if (savedUser != null && savedUser.getId() != null) {
                // Log a success message or return a success view
                System.out.println("User saved successfully. ID: " + savedUser.getId());
                return "redirect:https://danovs-autoshow-afcbab0f302b.herokuapp.com/login";
            } else  {
                System.err.println("Error saving user to the database");
                return "redirect:/register?error";
            }
        } else {
            // Log an error message or return an error view
            System.err.println("Error saving user to the database");
            return "redirect:/register?error";
        }
    }
}