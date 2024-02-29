package com.example.app.emailApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/reset-password")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;
    private final PasswordResetTokenRepository tokenRepository;

    @Autowired
    public PasswordResetController(PasswordResetService passwordResetService,
                                   PasswordResetTokenRepository tokenRepository) {
        this.passwordResetService = passwordResetService;
        this.tokenRepository = tokenRepository;
    }

    @GetMapping
    public ResponseEntity<String> resetPassword(@RequestParam String token,String newPassword) {
        // Validate Token
        Optional<PasswordResetToken> resetTokenOptional = tokenRepository.findByToken(token);
        if (resetTokenOptional.isEmpty() || resetTokenOptional.get().isExpired()) {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }

        // Reset the user password
        PasswordResetToken resetToken = resetTokenOptional.get();
        passwordResetService.updateUserPassword(resetToken.getUser(), newPassword);

        // Invalidate the used token
        tokenRepository.delete(resetToken);
        return ResponseEntity.ok("Password reset successful!");
    }

}
