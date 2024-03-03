package com.example.app.data.repositories;

import com.example.app.data.entities.User;
import com.example.app.data.entities.RecentlyViewedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecentlyViewedRepository extends JpaRepository<RecentlyViewedToken, Long> {

    Optional<RecentlyViewedToken> findByUser(User user);

    Optional<RecentlyViewedToken> findByUser_Id(Long userId);
}
