package com.example.app.services;

import com.example.app.data.entities.User;
import org.springframework.web.bind.annotation.ModelAttribute;

public interface RegistrationControllerService {

    String processRegistration(@ModelAttribute("user") User user);

}
