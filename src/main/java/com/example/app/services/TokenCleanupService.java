package com.example.app.services;

import org.springframework.scheduling.annotation.Scheduled;

public interface TokenCleanupService {

    @Scheduled(cron = "0 */30 * * * *") // Run every 30 mins
    void removeExpiredTokens();

}
