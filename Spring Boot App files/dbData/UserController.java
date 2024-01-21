package com.example.demo.dbData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUser() {
        return this.userService.userData();
    }
<<<<<<< HEAD
=======

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

>>>>>>> 01852e0dc7d90c77ffa7d99930dce7b1ae748c81
}
