package com.example.app.dbData.ReplacedTokens;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
public class DeleteReplacedTokensService {

    private final ScheduledExecutorService scheduler;

    private final Map<String, ScheduledFuture<?>> scheduledTasks;

    public DeleteReplacedTokensService() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.scheduledTasks = new HashMap<>();
    }

    public void scheduleDeletion(ReplacedAuthTokensRepo replacedAuthTokensRepo,
                                  String token) {
        ScheduledFuture<?> future = scheduler.schedule(() ->
                replacedAuthTokensRepo.deleteByReplacedToken(token), 3, TimeUnit.SECONDS);
        scheduledTasks.put(token,future);
    }
}
