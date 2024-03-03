package com.example.app.controllers;

import com.example.app.services.PasswordResetControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reset-password")
public class PasswordResetController {

    private final PasswordResetControllerService passwordResetControllerService;

    @Autowired
    public PasswordResetController(PasswordResetControllerService passwordResetControllerService) {
        this.passwordResetControllerService = passwordResetControllerService;
    }

    @GetMapping
    public ResponseEntity<String> resetPassword(@RequestParam String token,String newPassword) {
       return this.passwordResetControllerService.resetPassword(token,newPassword);
    }

}
