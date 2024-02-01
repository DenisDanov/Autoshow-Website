package com.example.demo.dbData.unsuccessfulLoginAttempts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedLoginAttemptsRepository extends JpaRepository<FailedLoginAttempts, Long> {

    FailedLoginAttempts findByUser_Id(Long userId);
}
