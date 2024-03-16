package com.example.app.services.impl;

import com.example.app.data.entities.RecentlyViewedToken;
import com.example.app.data.entities.User;
import com.example.app.data.repositories.RecentlyViewedRepository;
import com.example.app.services.RecentlyViewedTokenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecentlyViewedTokenServiceImpl implements RecentlyViewedTokenService {

    private final RecentlyViewedRepository recentlyViewedRepository;

    public RecentlyViewedTokenServiceImpl(RecentlyViewedRepository recentlyViewedRepository) {
        this.recentlyViewedRepository = recentlyViewedRepository;
    }

    @Override
    public Optional<RecentlyViewedToken> findByUser_Id(long id) {
        return this.recentlyViewedRepository.findByUser_Id(id);
    }

    @Override
    public Optional<RecentlyViewedToken> findByUser(User user) {
        return this.recentlyViewedRepository.findByUser(user);
    }

    @Override
    public Optional<RecentlyViewedToken> findByUser_Id(Long userId) {
        return this.recentlyViewedRepository.findByUser_Id(userId);
    }

    @Override
    public void save(RecentlyViewedToken recentlyViewedToken) {
        this.recentlyViewedRepository.save(recentlyViewedToken);
    }

    @Override
    public void delete(RecentlyViewedToken recentlyViewedToken) {
        this.recentlyViewedRepository.delete(recentlyViewedToken);
    }

    @Override
    public List<RecentlyViewedToken> findAll() {
        return this.recentlyViewedRepository.findAll();
    }

    @Override
    public void deleteAll(List<RecentlyViewedToken> recentlyViewedTokens) {
        this.recentlyViewedRepository.deleteAll(recentlyViewedTokens);
    }
}
