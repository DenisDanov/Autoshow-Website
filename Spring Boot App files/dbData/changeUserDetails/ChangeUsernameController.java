package com.example.demo.dbData.changeUserDetails;

import com.example.demo.dbData.User;
import com.example.demo.dbData.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
public class ChangeUsernameController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/changeUsername")
    public ResponseEntity<UsernameResponse> changeUsername(@RequestBody UsernameChangeRequest request) {
        try {
            // Validate the request
            if (request.getUserId() == null || request.getUsername() == null) {
                return ResponseEntity.badRequest().body(new UsernameResponse("Invalid request"));
            }

            // Find the user by ID
            Long userId = Long.parseLong(request.getUserId());
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getPassword().equals(request.getPassword())) {
                    if (userRepository.findByUsername(request.getUsername()) == null) {
                        user.setUsername(request.getUsername());
                        userRepository.save(user);
                        return ResponseEntity.ok(new UsernameResponse("Successfully changed the username."));
                    } else {
                        return ResponseEntity.ok(new UsernameResponse("This username already exists."));
                    }
                } else {
                    return ResponseEntity.ok(new UsernameResponse("Incorrect password."));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UsernameResponse("User not found."));
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., DataAccessException)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UsernameResponse("Error changing username."));
        }
    }

    @PostMapping("/changeEmail")
    public ResponseEntity<UsernameResponse> changeEmail(@RequestBody EmailRequest request) {
        try {
            // Validate the request
            if (request.getId() == null) {
                return ResponseEntity.badRequest().body(new UsernameResponse("Invalid request"));
            }

            // Find the user by ID
            Long userId = Long.parseLong(request.getId());
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setEmail(request.getEmail());
                userRepository.save(user);
                return ResponseEntity.ok(new UsernameResponse("Successfully changed the email."));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UsernameResponse("User not found."));
            }
        } catch (
                Exception e) {
            // Handle exceptions (e.g., DataAccessException)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UsernameResponse("Error changing email."));
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<UsernameResponse> changePassword(@RequestBody PasswordRequest request) {
        try {
            // Validate the request
            if (request.getId() == null) {
                return ResponseEntity.badRequest().body(new UsernameResponse("Invalid request"));
            }

            // Find the user by ID
            Long userId = Long.parseLong(request.getId());
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getPassword().equals(request.getCurrentPassword())) {
                    user.setPassword(request.getNewPassword());
                    userRepository.save(user);
                    return ResponseEntity.ok(new UsernameResponse("Successfully changed the password."));
                } else {
                    return ResponseEntity.ok(new UsernameResponse("Wrong password."));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UsernameResponse("User not found."));
            }
        } catch (
                Exception e) {
            // Handle exceptions (e.g., DataAccessException)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UsernameResponse("Error changing password."));
        }
    }
}
