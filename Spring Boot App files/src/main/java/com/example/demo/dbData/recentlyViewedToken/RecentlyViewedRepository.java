package com.example.demo.dbData.recentlyViewedToken;

import com.example.demo.dbData.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecentlyViewedRepository extends JpaRepository<RecentlyViewedToken, Long> {

    Optional<RecentlyViewedToken> findByUser(User user);

    Optional<RecentlyViewedToken> findByUser_Id(Long userId);
}
