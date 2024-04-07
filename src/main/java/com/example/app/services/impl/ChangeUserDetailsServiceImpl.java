package com.example.app.services.impl;

import com.example.app.data.DTOs.EmailRequestDTO;
import com.example.app.data.DTOs.PasswordRequestDTO;
import com.example.app.data.DTOs.UsernameChangeRequestDTO;
import com.example.app.data.DTOs.UsernameResponseDTO;
import com.example.app.data.entities.AuthenticationToken;
import com.example.app.data.entities.User;
import com.example.app.services.AuthenticationTokenService;
import com.example.app.services.ChangeUserDetailService;
import com.example.app.services.ReplacedAuthTokensService;
import com.example.app.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ChangeUserDetailsServiceImpl implements ChangeUserDetailService {

    private final UserService userService;

    private final AuthenticationTokenService authenticationTokenService;

    public ChangeUserDetailsServiceImpl(UserService userService,
                                        AuthenticationTokenService authenticationTokenService,
                                        ReplacedAuthTokensService replacedAuthTokensService) {
        this.userService = userService;
        this.authenticationTokenService = authenticationTokenService;
    }

    @Override
    public ResponseEntity<UsernameResponseDTO> changeUsername(UsernameChangeRequestDTO request, HttpServletResponse response) {
        try {
            // Validate the request
            if (request.getUserId() == null || request.getUsername() == null) {
                return ResponseEntity.badRequest().body(new UsernameResponseDTO("Invalid request"));
            }

            // Find the user by ID
            Long userId = Long.parseLong(request.getUserId());
            Optional<User> userOptional = userService.findById(userId);
            AuthenticationToken authenticationToken = authenticationTokenService.findByToken(request.getAuthToken());
            if (userOptional.isPresent() && authenticationToken != null &&
                    Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
                User user = userOptional.get();
                if (user.getPassword().equals(request.getPassword())) {
                    if (userService.findByUsername(request.getUsername()) == null) {
                        user.setUsername(request.getUsername());
                        userService.save(user);
                        return ResponseEntity.ok(new UsernameResponseDTO("Successfully changed the username."));
                    } else {
                        return ResponseEntity.ok(new UsernameResponseDTO("This username already exists."));
                    }
                } else {
                    return ResponseEntity.ok(new UsernameResponseDTO("Incorrect password."));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UsernameResponseDTO("User not found."));
            }
        } catch (Exception e) {
            // exceptions (e.g., DataAccessException)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UsernameResponseDTO("Error changing username."));
        }
    }

    @Override
    public ResponseEntity<UsernameResponseDTO> changeEmail(EmailRequestDTO request, HttpServletResponse response) {
        String emailRegex = "^[A-Za-z0-9]+[-._A-Za-z0-9]{0,}[A-Za-z0-9.-_]{0,}[@]{1}[A-Za-z-_]+[.]{1}[A-Za-z-_.]+[A-Za-z.-_]{0,}$";
        try {
            // Validate the request
            if (request.getId() == null) {
                return ResponseEntity.badRequest().body(new UsernameResponseDTO("Invalid request"));
            }

            // Find the user by ID
            Long userId = Long.parseLong(request.getId());
            Optional<User> userOptional = userService.findById(userId);
            AuthenticationToken authenticationToken = authenticationTokenService.findByToken(request.getAuthToken());
            if (userOptional.isPresent() && authenticationToken != null &&
                    Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
                if (request.getEmail().matches(emailRegex)) {
                    User user = userOptional.get();
                    if (userService.findByEmail(request.getEmail()) == null) {
                        user.setEmail(request.getEmail());
                        userService.save(user);
                        return ResponseEntity.ok(new UsernameResponseDTO("Successfully changed the email."));
                    } else {
                        return ResponseEntity.ok(new UsernameResponseDTO("Account with this email already exists."));
                    }
                } else {
                    return ResponseEntity.ok(new UsernameResponseDTO("Please use a valid email."));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UsernameResponseDTO("User not found."));
            }
        } catch (
                Exception e) {
            // Handle exceptions (e.g., DataAccessException)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UsernameResponseDTO("Error changing email."));
        }
    }

    @Override
    public ResponseEntity<UsernameResponseDTO> changePassword(PasswordRequestDTO request, HttpServletResponse response) {
        try {
            // Validate the request
            if (request.getId() == null) {
                return ResponseEntity.badRequest().body(new UsernameResponseDTO("Invalid request"));
            }

            // Find the user by ID
            Long userId = Long.parseLong(request.getId());
            Optional<User> userOptional = userService.findById(userId);
            AuthenticationToken authenticationToken = authenticationTokenService.findByToken(request.getAuthToken());
            if (userOptional.isPresent() && authenticationToken != null &&
                    Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
                User user = userOptional.get();
                if (user.getPassword().equals(request.getCurrentPassword())) {
                    if (request.getNewPassword().length() >= 8) {
                        user.setPassword(request.getNewPassword());
                        userService.save(user);
                        return ResponseEntity.ok(new UsernameResponseDTO("Successfully changed the password."));
                    } else {
                        return ResponseEntity.ok(new UsernameResponseDTO("Please use password with at least 8 characters."));
                    }
                } else {
                    return ResponseEntity.ok(new UsernameResponseDTO("Wrong password."));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UsernameResponseDTO("User not found."));
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., DataAccessException)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UsernameResponseDTO("Error changing password."));
        }
    }
}
