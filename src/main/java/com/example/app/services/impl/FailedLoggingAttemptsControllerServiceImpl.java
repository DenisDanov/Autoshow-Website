package com.example.app.services.impl;

import com.example.app.services.FailedLoggingAttemptsControllerService;
import com.example.app.services.FailedLoginAttemptsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FailedLoggingAttemptsControllerServiceImpl implements FailedLoggingAttemptsControllerService {

    private final FailedLoginAttemptsService failedLoginAttemptsService;

    public FailedLoggingAttemptsControllerServiceImpl(FailedLoginAttemptsService failedLoginAttemptsService) {
        this.failedLoginAttemptsService = failedLoginAttemptsService;
    }

    @Override
    public ResponseEntity<String> removeExpiredAttempts(Map<String, Object> requestBody) {
        Long userId = Long.parseLong(requestBody.get("id").toString());
        if (failedLoginAttemptsService.findByUser_Id(userId) != null) {
            failedLoginAttemptsService.delete(failedLoginAttemptsService.findByUser_Id(userId));
            return ResponseEntity.ok("Successfully removed entity");
        } else {
            return ResponseEntity.ok("Entity doesn't exist");
        }
    }
}
