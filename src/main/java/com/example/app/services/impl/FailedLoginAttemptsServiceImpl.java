package com.example.app.services.impl;

import com.example.app.data.entities.FailedLoginAttempts;
import com.example.app.data.repositories.FailedLoginAttemptsRepository;
import com.example.app.services.FailedLoginAttemptsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FailedLoginAttemptsServiceImpl implements FailedLoginAttemptsService {

    private final FailedLoginAttemptsRepository failedLoginAttemptsRepository;

    public FailedLoginAttemptsServiceImpl(FailedLoginAttemptsRepository failedLoginAttemptsRepository) {
        this.failedLoginAttemptsRepository = failedLoginAttemptsRepository;
    }

    @Override
    public FailedLoginAttempts findByUser_Id(Long userId) {
        return this.failedLoginAttemptsRepository.findByUser_Id(userId);
    }

    @Override
    public int deleteByUser_Id(Long userId) {
        return this.failedLoginAttemptsRepository.deleteByUser_Id(userId);
    }

    @Override
    public int deleteByUser_Username(String username) {
        return this.failedLoginAttemptsRepository.deleteByUser_Username(username);
    }

    @Override
    public FailedLoginAttempts save(FailedLoginAttempts failedLoginAttempts) {
        return this.failedLoginAttemptsRepository.save(failedLoginAttempts);
    }

    @Override
    public void delete(FailedLoginAttempts failedLoginAttempts) {
        this.failedLoginAttemptsRepository.delete(failedLoginAttempts);
    }

    @Override
    public List<FailedLoginAttempts> findAll() {
        return this.failedLoginAttemptsRepository.findAll();
    }
}
