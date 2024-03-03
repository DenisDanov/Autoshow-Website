package com.example.app.services.impl;

import com.example.app.data.entities.User;
import com.example.app.services.PasswordResetTokenService;
import com.example.app.services.UserControllerService;
import com.example.app.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserControllerServiceImpl implements UserControllerService {

    private final UserService userService;

    private final PasswordResetTokenService passwordResetTokenService;

    public UserControllerServiceImpl(UserService userService,
                                     PasswordResetTokenService passwordResetTokenService) {
        this.userService = userService;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @Override
    public ResponseEntity<String> initiatePasswordReset(String email) throws IOException {
        User user = userService.findByEmail(email);

        if (user != null) {
            if (passwordResetTokenService.findByUser(user).isEmpty()) {
                passwordResetTokenService.initiatePasswordReset(user);
                return ResponseEntity.ok("Password reset initiated. Check your email for further instructions.");
            } else {
                return ResponseEntity.ok("Password reset have already been requested on this email. Please check the email for further instructions.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The account with this email could not be found. Please try with different email.");
        }
    }
}
