package com.example.demo.dbData.unsuccessfulLoginAttempts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/failed-login-attempts")
public class FailedLoginAttemptsController {

    @Autowired
    private FailedLoginAttemptsRepository failedLoginAttemptsRepository;

    @DeleteMapping("/remove")
    private ResponseEntity<String> removeExpiredAttempts(@RequestBody Map<String, Object> requestBody) {
        Long userId = Long.parseLong(requestBody.get("id").toString());
        if (failedLoginAttemptsRepository.findByUser_Id(userId) != null) {
            failedLoginAttemptsRepository.delete(failedLoginAttemptsRepository.findByUser_Id(userId));
            return ResponseEntity.ok("Successfully removed entity");
        } else {
            return ResponseEntity.ok("Entity doesn't exist");
        }
    }
}
