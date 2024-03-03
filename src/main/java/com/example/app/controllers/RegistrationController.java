package com.example.app.controllers;

import com.example.app.data.entities.User;
import com.example.app.services.RegistrationControllerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final RegistrationControllerService registrationControllerService;

    public RegistrationController(RegistrationControllerService registrationControllerService) {
        this.registrationControllerService = registrationControllerService;
    }

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") User user) {
       return this.registrationControllerService.processRegistration(user);
    }
}