package com.example.app.controllers;

import com.example.app.services.FailedLoggingAttemptsControllerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/failed-login-attempts")
public class FailedLoginAttemptsController {

    private final FailedLoggingAttemptsControllerService failedLoggingAttemptsControllerService;

    public FailedLoginAttemptsController(FailedLoggingAttemptsControllerService failedLoggingAttemptsControllerService) {
        this.failedLoggingAttemptsControllerService = failedLoggingAttemptsControllerService;
    }

    @DeleteMapping("/remove")
    private ResponseEntity<String> removeExpiredAttempts(@RequestBody Map<String, Object> requestBody) {
        return failedLoggingAttemptsControllerService.removeExpiredAttempts(requestBody);
    }
}
