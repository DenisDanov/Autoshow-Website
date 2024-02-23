package com.example.demo.emailApp;

import com.example.demo.dbData.User;
import com.example.demo.dbData.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepository;

    private final SendGridEmailService sendGridEmailService;

    private final UserRepository userRepository;

    @Autowired
    public PasswordResetService(PasswordResetTokenRepository tokenRepository,
                                SendGridEmailService sendGridEmailService,
                                UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.sendGridEmailService = sendGridEmailService;
        this.userRepository = userRepository;
    }

    public void initiatePasswordReset(User user) throws IOException {
        String resetToken = generateResetToken(user);
        sendPasswordResetEmail(user.getEmail(), resetToken);
    }

    private void sendPasswordResetEmail(String email, String resetToken) throws IOException {
        String resetLink = "https://danov-autoshow-656625355b99.herokuapp.com/reset-password.html?token=" + resetToken;
        String subject = "Password Reset Request";
        String text = "You have 24 hours to reset your password. To proceed, please click the link below:\n" + resetLink;
        sendGridEmailService.sendSimpleMessage(email, subject, text);
    }

    private String generateResetToken(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(Instant.now().plusSeconds(86400)); // Token expiration time (24 hours)
        tokenRepository.save(resetToken);
        return token;
    }

    public void updateUserPassword(User user, String newPassword) {
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public void removeExpiredTokens() {
        Instant now = Instant.now();
        List<PasswordResetToken> expiredTokens = tokenRepository.findAll()
                .stream()
                .filter(token -> token.getExpiryDate().isBefore(now))
                .collect(Collectors.toList());
        tokenRepository.deleteAll(expiredTokens);
    }

}
