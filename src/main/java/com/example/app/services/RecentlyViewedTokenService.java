package com.example.app.services;

import com.example.app.data.entities.RecentlyViewedToken;
import com.example.app.data.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RecentlyViewedTokenService {

    Optional<RecentlyViewedToken> findByUser_Id(long id);

    Optional<RecentlyViewedToken> findByUser(User user);

    Optional<RecentlyViewedToken> findByUser_Id(Long userId);

    void save(RecentlyViewedToken recentlyViewedToken);

    void delete(RecentlyViewedToken recentlyViewedToken);

    List<RecentlyViewedToken> findAll();

    void deleteAll(List<RecentlyViewedToken> recentlyViewedTokens);
}
