package com.example.demo.dbData;

import com.example.demo.dbData.ReplacedTokens.ReplacedAuthTokens;
import com.example.demo.dbData.ReplacedTokens.ReplacedAuthTokensRepo;
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

    @Autowired
    private ReplacedAuthTokensRepo replacedAuthTokensRepo;

    @Scheduled(cron = "0 */35 * * * *")
    public void cleanUpExpiredTokens() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        List<AuthenticationToken> expiredTokens = authenticationTokensRepository.findAll()
                .stream()
                .filter(token -> currentTimestamp.after(token.getExpireDate()))
                .collect(Collectors.toList());
        List<ReplacedAuthTokens> expiredReplacedTokens = replacedAuthTokensRepo.findAll()
                .stream()
                .filter(token -> currentTimestamp.after(token.getExpireDate()))
                .collect(Collectors.toList());
        replacedAuthTokensRepo.deleteAll(expiredReplacedTokens);
        authenticationTokensRepository.deleteAll(expiredTokens);
    }
}
