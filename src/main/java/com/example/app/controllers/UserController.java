package com.example.app.controllers;

import com.example.app.services.UserControllerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserControllerService userControllerService;

    public UserController(UserControllerService userControllerService) {
        this.userControllerService = userControllerService;
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<String> initiatePasswordReset(@RequestParam String email) throws IOException {
        return this.userControllerService.initiatePasswordReset(email);
    }

}
