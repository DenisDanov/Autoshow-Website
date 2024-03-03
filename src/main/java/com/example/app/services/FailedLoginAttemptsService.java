package com.example.app.services;

import com.example.app.data.entities.FailedLoginAttempts;

import java.util.List;

public interface FailedLoginAttemptsService {

    FailedLoginAttempts findByUser_Id(Long userId);

    int deleteByUser_Id(Long userId);

    int deleteByUser_Username(String username);

    FailedLoginAttempts save(FailedLoginAttempts failedLoginAttempts);

    void delete(FailedLoginAttempts failedLoginAttempts);

    List<FailedLoginAttempts> findAll();
}
