package com.example.app.services.impl;

import com.example.app.data.entities.ReplacedAuthToken;
import com.example.app.data.entities.User;
import com.example.app.data.repositories.ReplacedAuthTokensRepo;
import com.example.app.services.ReplacedAuthTokensService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplacedAuthTokensServiceImpl implements ReplacedAuthTokensService {

    private final ReplacedAuthTokensRepo replacedAuthTokensRepo;

    public ReplacedAuthTokensServiceImpl(ReplacedAuthTokensRepo replacedAuthTokensRepo) {
        this.replacedAuthTokensRepo = replacedAuthTokensRepo;
    }

    @Override
    public List<ReplacedAuthToken> findByUser(User user) {
        return this.replacedAuthTokensRepo.findByUser(user);
    }

    @Override
    public ReplacedAuthToken findByReplacedToken(String replacedToken) {
        return this.replacedAuthTokensRepo.findByReplacedToken(replacedToken);
    }

    @Override
    public void deleteByReplacedToken(String token) {
        this.replacedAuthTokensRepo.deleteByReplacedToken(token);
    }

    @Override
    public void save(ReplacedAuthToken replacedAuthToken) {
        this.replacedAuthTokensRepo.save(replacedAuthToken);
    }

    @Override
    public List<ReplacedAuthToken> findAll() {
        return this.replacedAuthTokensRepo.findAll();
    }

    @Override
    public void deleteAll(List<ReplacedAuthToken> replacedAuthTokens) {
        this.replacedAuthTokensRepo.deleteAll(replacedAuthTokens);
    }
}
