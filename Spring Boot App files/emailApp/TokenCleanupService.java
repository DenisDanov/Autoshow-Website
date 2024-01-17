package com.example.demo.emailApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TokenCleanupService {

    @Autowired
    private PasswordResetService passwordResetService;

    @Scheduled(cron = "0 0 * * * *") // Run every hour
    public void removeExpiredTokens() {
        passwordResetService.removeExpiredTokens();
    }
}
