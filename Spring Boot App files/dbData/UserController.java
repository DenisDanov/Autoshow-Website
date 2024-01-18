package com.example.demo.dbData;

import com.example.demo.emailApp.PasswordResetService;
import com.example.demo.emailApp.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {
    private final UserService userService;

    private final UserRepository userRepository;

    private PasswordResetService passwordResetService;

    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    public UserController(UserService userService,
                          UserRepository userRepository,
                          PasswordResetService passwordResetService,
                          PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordResetService = passwordResetService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @GetMapping
    public List<User> getUser() {
        return this.userService.userData();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> initiatePasswordReset(@RequestParam String email) throws IOException {
        // Assuming you have a User entity with an 'email' field
        User user = userRepository.findByEmail(email);

        if (user != null) {
            if (passwordResetTokenRepository.findByUser(user) == null) {
                passwordResetService.initiatePasswordReset(user);
                return ResponseEntity.ok("Password reset initiated. Check your email for further instructions.");
            } else {
                return ResponseEntity.ok("You have already requested password reset. Please check your email.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

}
