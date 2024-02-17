package com.example.demo.dbData.recentlyViewedToken;

import com.example.demo.dbData.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecentlyViewedRepository extends JpaRepository<RecentlyViewedToken, Long> {

    Optional<RecentlyViewedToken> findByUser(User user);

    Optional<RecentlyViewedToken> findByUser_Id(Long userId);
}
