package com.example.app.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface PasswordResetControllerService {

    ResponseEntity<String> resetPassword(@RequestParam String token, String newPassword);

}
