package com.example.demo.dbData.unsuccessfulLoginAttempts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.util.Date;
import java.util.concurrent.*;

@Service
public class ExpiringEntityDeleter {

    private final ScheduledExecutorService scheduler;
    private final FailedLoginAttemptsRepository failedLoginAttemptsRepository;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks;

    @Autowired
    public ExpiringEntityDeleter(FailedLoginAttemptsRepository failedLoginAttemptsRepository) {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.failedLoginAttemptsRepository = failedLoginAttemptsRepository;
        this.scheduledTasks = new HashMap<>();
    }

    public void scheduleDeletion(FailedLoginAttempts failedLoginAttempts) {
        long delay = calculateDelay(failedLoginAttempts.getAccountLockExpireTime()) + 250;
        System.out.println(failedLoginAttempts.getAccountLockExpireTime());
        // Schedule the deletion task and store the ScheduledFuture
        ScheduledFuture<?> future = scheduler.schedule(() -> deleteEntity(failedLoginAttempts), delay, TimeUnit.MILLISECONDS);
        scheduledTasks.put(failedLoginAttempts.getUser().getId(), future);
    }

    public void cancelScheduledDeletion(FailedLoginAttempts failedLoginAttempts) {
        // Retrieve the ScheduledFuture for the given failedLoginAttempts
        ScheduledFuture<?> future = scheduledTasks.get(failedLoginAttempts.getUser().getId());
        if (future != null) {
            // Cancel the scheduled task
            System.out.println(future.cancel(false));
            // Remove the entry from the map
            scheduledTasks.remove(failedLoginAttempts);
        }
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
