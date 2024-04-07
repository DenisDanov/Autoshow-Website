package com.example.app.controllers;

import com.example.app.services.UserControllerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserControllerService userControllerService;

    public UserController(UserControllerService userControllerService) {
        this.userControllerService = userControllerService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> initiatePasswordReset(@RequestParam String email) throws IOException {
        return this.userControllerService.initiatePasswordReset(email);
    }

}
