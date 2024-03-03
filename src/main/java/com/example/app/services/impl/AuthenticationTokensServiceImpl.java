package com.example.app.services.impl;

import com.example.app.data.entities.AuthenticationToken;
import com.example.app.data.entities.ReplacedAuthToken;
import com.example.app.data.repositories.AuthenticationTokenRepository;
import com.example.app.services.AuthenticationTokenService;
import com.example.app.services.ReplacedAuthTokensService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthenticationTokensServiceImpl implements AuthenticationTokenService {

    private final AuthenticationTokenRepository authenticationTokenRepository;

    private final ReplacedAuthTokensService replacedAuthTokensService;

    public AuthenticationTokensServiceImpl(AuthenticationTokenRepository authenticationTokenRepository,
                                           ReplacedAuthTokensService replacedAuthTokensService) {
        this.authenticationTokenRepository = authenticationTokenRepository;
        this.replacedAuthTokensService = replacedAuthTokensService;
    }

    @Override
    @Scheduled(cron = "0 */35 * * * *")
    public void cleanUpExpiredTokens() {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        List<AuthenticationToken> expiredTokens = authenticationTokenRepository.findAll()
                .stream()
                .filter(token -> currentTimestamp.after(token.getExpireDate()))
                .collect(Collectors.toList());
        List<ReplacedAuthToken> expiredReplacedTokens = replacedAuthTokensService.findAll()
                .stream()
                .filter(token -> currentTimestamp.after(token.getExpireDate()))
                .collect(Collectors.toList());
        replacedAuthTokensService.deleteAll(expiredReplacedTokens);
        authenticationTokenRepository.deleteAll(expiredTokens);
    }

    @Override
    public AuthenticationToken findByUser_Id(Long userId) {
        return authenticationTokenRepository.findByUser_Id(userId);
    }

    @Override
    public AuthenticationToken findByToken(String token) {
        return authenticationTokenRepository.findByToken(token);
    }

    @Override
    public int updateUserToken(Long userId, String token, Timestamp newDate) {
        return authenticationTokenRepository.updateUserToken(userId,token,newDate);
    }

    @Override
    public AuthenticationToken save(AuthenticationToken authenticationToken) {
        return authenticationTokenRepository.save(authenticationToken);
    }
}
