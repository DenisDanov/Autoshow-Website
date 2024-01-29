package com.example.demo.dbData;

import com.example.demo.emailApp.PasswordResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationTokensService {

    @Autowired
    private AuthenticationTokensRepository authenticationTokensRepository;

    @Scheduled(cron = "0 */35 * * * *")
    public void cleanUpExpiredTokens() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        List<AuthenticationToken> expiredTokens = authenticationTokensRepository.findAll()
                .stream()
                .filter(token -> currentTimestamp.after(token.getExpireDate()))
                .collect(Collectors.toList());
        authenticationTokensRepository.deleteAll(expiredTokens);
    }
}
