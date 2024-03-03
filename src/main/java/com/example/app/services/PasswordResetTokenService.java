package com.example.app.services;

import com.example.app.data.entities.PasswordResetToken;
import com.example.app.data.entities.User;

import java.io.IOException;
import java.util.Optional;


public interface PasswordResetTokenService {

    void initiatePasswordReset(User user) throws IOException;

    void updateUserPassword(User user, String newPassword);

    void removeExpiredTokens();

    Optional<PasswordResetToken> findByToken(String token);

    void delete(PasswordResetToken passwordResetToken);

    Optional<PasswordResetToken> findByUser(User user);
}
