package com.example.app.services.impl;

import com.example.app.data.entities.PasswordResetToken;
import com.example.app.services.PasswordResetControllerService;
import com.example.app.services.PasswordResetTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PasswordResetControllerServiceImpl implements PasswordResetControllerService {

    private final PasswordResetTokenService passwordResetTokenService;

    public PasswordResetControllerServiceImpl(PasswordResetTokenService passwordResetTokenService) {
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @Override
    public ResponseEntity<String> resetPassword(String token, String newPassword) {
        // Validate Token
        Optional<PasswordResetToken> resetTokenOptional = passwordResetTokenService.findByToken(token);
        if (resetTokenOptional.isEmpty() || resetTokenOptional.get().isExpired()) {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }

        // Reset the user password
        PasswordResetToken resetToken = resetTokenOptional.get();
        passwordResetTokenService.updateUserPassword(resetToken.getUser(), newPassword);

        // Invalidate the used token
        passwordResetTokenService.delete(resetToken);
        return ResponseEntity.ok("Password reset successful!");
    }
}
