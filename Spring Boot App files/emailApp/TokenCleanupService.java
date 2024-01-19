package com.example.demo.emailApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TokenCleanupService {

    @Autowired
    private PasswordResetService passwordResetService;

    @Scheduled(cron = "0 */30 * * * *") // Run every 30 mins
    public void removeExpiredTokens() {
        passwordResetService.removeExpiredTokens();
    }
}
