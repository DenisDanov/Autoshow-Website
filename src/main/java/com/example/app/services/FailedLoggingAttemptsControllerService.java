package com.example.app.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface FailedLoggingAttemptsControllerService {

    ResponseEntity<String> removeExpiredAttempts(@RequestBody Map<String, Object> requestBody);

}
