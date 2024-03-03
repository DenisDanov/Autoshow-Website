package com.example.app.services.impl;

import com.example.app.services.DeleteReplacedTokensService;
import com.example.app.services.ReplacedAuthTokensService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class DeleteReplacedTokensServiceImpl implements DeleteReplacedTokensService {

    private final ScheduledExecutorService scheduler;

    private final Map<String, ScheduledFuture<?>> scheduledTasks;

    private final ReplacedAuthTokensService replacedAuthTokensService;

    public DeleteReplacedTokensServiceImpl(ReplacedAuthTokensService replacedAuthTokensService) {
        this.replacedAuthTokensService = replacedAuthTokensService;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.scheduledTasks = new HashMap<>();
    }

    @Override
    public void scheduleDeletion(String token) {
        ScheduledFuture<?> future = scheduler.schedule(() ->
                replacedAuthTokensService.deleteByReplacedToken(token), 3, TimeUnit.SECONDS);
        scheduledTasks.put(token,future);
    }
}
