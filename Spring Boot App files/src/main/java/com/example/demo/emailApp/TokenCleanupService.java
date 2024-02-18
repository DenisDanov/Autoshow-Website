package com.example.demo.emailApp;

import com.example.demo.dbData.recentlyViewedToken.RecentlyViewedRepository;
import com.example.demo.dbData.recentlyViewedToken.RecentlyViewedToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TokenCleanupService {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private RecentlyViewedRepository recentlyViewedRepository;

    @Scheduled(cron = "0 */30 * * * *") // Run every 30 mins
    public void removeExpiredTokens() {
        passwordResetService.removeExpiredTokens();
        Instant now = Instant.now();
        List<RecentlyViewedToken> expiredTokens = recentlyViewedRepository.findAll()
                .stream()
                .filter(token -> token.getExpireDate().toInstant().isBefore(now))
                .collect(Collectors.toList());
        recentlyViewedRepository.deleteAll(expiredTokens);
    }
}
