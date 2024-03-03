package com.example.app.data.repositories;

import com.example.app.data.entities.FailedLoginAttempts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedLoginAttemptsRepository extends JpaRepository<FailedLoginAttempts, Long> {

    FailedLoginAttempts findByUser_Id(Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM FailedLoginAttempts fa WHERE fa.user.id = :userId")
    int deleteByUser_Id(Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM FailedLoginAttempts fa WHERE fa.user.username = :username")
    int deleteByUser_Username(String username);
}
