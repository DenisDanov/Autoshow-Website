package com.example.app.services.impl;

import com.example.app.data.entities.FailedLoginAttempts;
import com.example.app.services.CarOrderSpecDataService;
import com.example.app.services.ExpiringEntityDeleterService;
import com.example.app.services.FailedLoginAttemptsService;
import com.example.app.services.ScheduleDeletingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class ScheduleDeletingImpl implements CommandLineRunner, ScheduleDeletingService {

    private final FailedLoginAttemptsService failedLoginAttemptsService;

    private final ExpiringEntityDeleterService expiringEntityDeleter;

    private final CarOrderSpecDataService carOrderSpecDataService;

    @Autowired
    public ScheduleDeletingImpl(FailedLoginAttemptsService failedLoginAttemptsService, CarOrderSpecDataService carOrderSpecDataService) {
        this.failedLoginAttemptsService = failedLoginAttemptsService;
        this.expiringEntityDeleter = new ExpiringEntityDeleterImpl(failedLoginAttemptsService);
        this.carOrderSpecDataService = carOrderSpecDataService;
    }

    @Override
    public void scheduleDeletingOfEntities() {
        for (FailedLoginAttempts failedLoginAttempts : this.failedLoginAttemptsService.findAll()) {
            this.expiringEntityDeleter.scheduleDeletion(failedLoginAttempts);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        this.scheduleDeletingOfEntities();
        this.carOrderSpecDataService.seedDatabase();
    }
}
