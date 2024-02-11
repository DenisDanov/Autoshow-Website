package com.example.demo.dbData;

import com.example.demo.emailApp.PasswordResetService;
import com.example.demo.emailApp.PasswordResetTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    private HttpServletRequest request;

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
        User user = userRepository.findByEmail(email);

        if (user != null) {
            if (passwordResetTokenRepository.findByUser(user).isEmpty()) {
                passwordResetService.initiatePasswordReset(user);
                return ResponseEntity.ok("Password reset initiated. Check your email for further instructions.");
            } else {
                return ResponseEntity.ok("Password reset have already been requested on this email. Please check the email for further instructions.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The account with this email could not be found. Please try with different email.");
        }
    }

    @GetMapping("/getUserId")
    public String getUserId() {
        // Retrieve the HttpSession
        HttpSession httpSession = request.getSession(false); // Don't create a new session if it doesn't exist

        // Check if HttpSession exists and retrieve userId attribute
        if (httpSession != null) {
            String userId = (String) httpSession.getAttribute("userId");
            if (userId != null) {
                return "User ID: " + userId;
            } else {
                return "User ID not found in session";
            }
        } else {
            return "No session found";
        }
    }

}
