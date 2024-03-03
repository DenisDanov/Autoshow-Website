package com.example.app.data.repositories;

import com.example.app.data.entities.AuthenticationToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Component
@Repository
public interface AuthenticationTokenRepository extends JpaRepository<AuthenticationToken, Long> {

    AuthenticationToken findByUser_Id(Long userId);

    AuthenticationToken findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE AuthenticationToken au SET au.token = :token, au.expireDate = :newDate WHERE au.user.id = :userId")
    int updateUserToken(Long userId, String token, Timestamp newDate);
}
