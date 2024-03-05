package com.example.app.services.impl;

import com.example.app.data.entities.RecentlyViewedToken;
import com.example.app.services.PasswordResetTokenService;
import com.example.app.services.RecentlyViewedTokenService;
import com.example.app.services.TokenCleanupService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenCleanupServiceImpl implements TokenCleanupService {

    private final PasswordResetTokenService passwordResetTokenService;

    private final RecentlyViewedTokenService recentlyViewedTokenService;

    public TokenCleanupServiceImpl(PasswordResetTokenService passwordResetTokenService,
                                   RecentlyViewedTokenService recentlyViewedTokenService) {
        this.passwordResetTokenService = passwordResetTokenService;
        this.recentlyViewedTokenService = recentlyViewedTokenService;
    }

    @Override
    @Scheduled(cron = "0 */30 * * * *") // Run every 30 mins
    public void removeExpiredTokens() {
        passwordResetTokenService.removeExpiredTokens();
        Instant now = Instant.now();
        List<RecentlyViewedToken> expiredTokens = recentlyViewedTokenService.findAll()
                .stream()
                .filter(token -> {
                    // Convert LocalDateTime to Instant
                    Instant expireInstant = token.getExpireDate().atZone
                            (ZoneId.systemDefault()).toInstant();
                    return expireInstant.isBefore(now);
                })
                .collect(Collectors.toList());
        recentlyViewedTokenService.deleteAll(expiredTokens);
    }
}
