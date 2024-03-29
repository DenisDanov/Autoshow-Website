package com.example.app.services.impl;

import com.example.app.data.entities.PasswordResetToken;
import com.example.app.data.entities.User;
import com.example.app.data.repositories.PasswordResetTokenRepository;
import com.example.app.services.PasswordResetTokenService;
import com.example.app.services.SendGridEmailService;
import com.example.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository tokenRepository;

    private final SendGridEmailService sendGridEmailService;

    private final UserService userService;

    @Autowired
    public PasswordResetTokenServiceImpl(PasswordResetTokenRepository tokenRepository,
                                         SendGridEmailService sendGridEmailService,
                                         UserService userService) {
        this.tokenRepository = tokenRepository;
        this.sendGridEmailService = sendGridEmailService;
        this.userService = userService;
    }

    @Override
    public void initiatePasswordReset(User user) throws IOException {
        String resetToken = generateResetToken(user);
        sendPasswordResetEmail(user.getEmail(), resetToken);
    }

    private void sendPasswordResetEmail(String email, String resetToken) throws IOException {
        String resetLink = "https://danov-autoshow.azurewebsites.net/reset-password.html?token=" + resetToken;
        String subject = "Password Reset Request";
        String text = "You have 24 hours to reset your password. To proceed, please click the link below:\n" + resetLink;
        sendGridEmailService.sendSimpleMessage(email, subject, text);
    }

    private String generateResetToken(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpireDate(Instant.now().plusSeconds(86400)); // Token expiration time (24 hours)
        tokenRepository.save(resetToken);
        return token;
    }

    @Override
    public void updateUserPassword(User user, String newPassword) {
        user.setPassword(newPassword);
        userService.save(user);
    }

    @Override
    public void removeExpiredTokens() {
        Instant now = Instant.now();
        List<PasswordResetToken> expiredTokens = tokenRepository.findAll()
                .stream()
                .filter(token -> token.getExpireDate().isBefore(now))
                .collect(Collectors.toList());
        tokenRepository.deleteAll(expiredTokens);
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return this.tokenRepository.findByToken(token);
    }

    @Override
    public void delete(PasswordResetToken passwordResetToken) {
        this.tokenRepository.delete(passwordResetToken);
    }

    @Override
    public Optional<PasswordResetToken> findByUser(User user) {
        return this.tokenRepository.findByUser(user);
    }

}
