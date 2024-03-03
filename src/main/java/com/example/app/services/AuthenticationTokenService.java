package com.example.app.services;

import com.example.app.data.entities.AuthenticationToken;
import java.sql.Timestamp;

public interface AuthenticationTokenService {

    void cleanUpExpiredTokens();

    AuthenticationToken findByUser_Id(Long userId);

    AuthenticationToken findByToken(String token);

    int updateUserToken(Long userId, String token, Timestamp newDate);

    AuthenticationToken save(AuthenticationToken authenticationToken);
}
