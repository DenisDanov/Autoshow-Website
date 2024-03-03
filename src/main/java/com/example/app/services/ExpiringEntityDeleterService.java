package com.example.app.services;

import com.example.app.data.entities.FailedLoginAttempts;
import org.springframework.stereotype.Service;


public interface ExpiringEntityDeleterService {

    void scheduleDeletion(FailedLoginAttempts failedLoginAttempts);

    void cancelScheduledDeletion(FailedLoginAttempts failedLoginAttempts);
}
