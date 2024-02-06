package com.example.demo.dbData.unsuccessfulLoginAttempts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ExpiringEntityDeleter {

    private final ScheduledExecutorService scheduler;

    private final FailedLoginAttemptsRepository failedLoginAttemptsRepository;

    @Autowired
    public ExpiringEntityDeleter(FailedLoginAttemptsRepository failedLoginAttemptsRepository) {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.failedLoginAttemptsRepository = failedLoginAttemptsRepository;
    }

    public void scheduleDeletion(FailedLoginAttempts failedLoginAttempts) {
        Date expirationDate = failedLoginAttempts.getAccountLockExpireTime();
        long delay = calculateDelay(expirationDate);

        // Schedule the deletion task
        scheduler.schedule(() -> deleteEntity(failedLoginAttempts), delay, TimeUnit.MILLISECONDS);
    }

    public void deleteEntityImmediately(FailedLoginAttempts failedLoginAttempts) {
        scheduler.schedule(() -> deleteEntity(failedLoginAttempts),0,TimeUnit.MILLISECONDS);
    }

    private long calculateDelay(Date expirationDate) {
        // Calculate the delay in milliseconds between current time and expiration time
        return expirationDate.getTime() - System.currentTimeMillis();
    }

    private void deleteEntity(FailedLoginAttempts failedLoginAttempts) {
        // delete the entity from the repository
        failedLoginAttemptsRepository.delete(failedLoginAttempts);
    }

}
