package com.example.demo.dbData.unsuccessfulLoginAttempts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class ScheduleDeleting implements CommandLineRunner {

    private final FailedLoginAttemptsRepository failedLoginAttemptsRepository;

    private final ExpiringEntityDeleter expiringEntityDeleter;

    @Autowired
    public ScheduleDeleting(FailedLoginAttemptsRepository failedLoginAttemptsRepository) {
        this.failedLoginAttemptsRepository = failedLoginAttemptsRepository;
        this.expiringEntityDeleter = new ExpiringEntityDeleter(failedLoginAttemptsRepository);
    }

    public void scheduleDeletingOfEntities() {
        for (FailedLoginAttempts failedLoginAttempts : this.failedLoginAttemptsRepository.findAll()) {
            this.expiringEntityDeleter.scheduleDeletion(failedLoginAttempts);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        this.scheduleDeletingOfEntities();
    }
}
