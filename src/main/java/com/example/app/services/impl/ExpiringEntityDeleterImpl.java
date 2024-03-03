package com.example.app.services.impl;

import com.example.app.data.entities.FailedLoginAttempts;
import com.example.app.services.ExpiringEntityDeleterService;
import com.example.app.services.FailedLoginAttemptsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.util.concurrent.*;

@Service
public class ExpiringEntityDeleterImpl implements ExpiringEntityDeleterService {

    private final ScheduledExecutorService scheduler;
    private final FailedLoginAttemptsService failedLoginAttemptsService;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks;

    public ExpiringEntityDeleterImpl(FailedLoginAttemptsService failedLoginAttemptsService) {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.failedLoginAttemptsService = failedLoginAttemptsService;
        this.scheduledTasks = new HashMap<>();
    }

    @Override
    public void scheduleDeletion(FailedLoginAttempts failedLoginAttempts) {
        long delay = calculateDelay(failedLoginAttempts.getAccountLockExpireTime()) + 250;
        // Schedule the deletion task and store the ScheduledFuture
        ScheduledFuture<?> future = scheduler.schedule(() -> failedLoginAttemptsService.delete(failedLoginAttempts), delay, TimeUnit.MILLISECONDS);
        scheduledTasks.put(failedLoginAttempts.getUser().getId(), future);
    }

    @Override
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

    private long calculateDelay(Date expirationDate) {
        // Calculate the delay in milliseconds between current time and expiration time
        return expirationDate.getTime() - System.currentTimeMillis();
    }

}
