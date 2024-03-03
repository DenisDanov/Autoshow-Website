package com.example.app.services;

import org.springframework.stereotype.Service;


public interface TokenCleanupService {

    void removeExpiredTokens();

}
