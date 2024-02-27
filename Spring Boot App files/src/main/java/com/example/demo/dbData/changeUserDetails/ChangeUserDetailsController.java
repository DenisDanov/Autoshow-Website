package com.example.demo.dbData.changeUserDetails;

import com.example.demo.dbData.AuthenticationToken;
import com.example.demo.dbData.AuthenticationTokenRepository;
import com.example.demo.dbData.ReplacedTokens.ReplacedAuthTokensRepo;
import com.example.demo.dbData.User;
import com.example.demo.dbData.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
public class ChangeUserDetailsController {

    private UserRepository userRepository;

    private AuthenticationTokenRepository authenticationTokenRepository;

    private ReplacedAuthTokensRepo replacedAuthTokensRepo;

    @Autowired
    public ChangeUserDetailsController(UserRepository userRepository,
                                       AuthenticationTokenRepository authenticationTokenRepository,
                                       ReplacedAuthTokensRepo replacedAuthTokensRepo) {
        this.userRepository = userRepository;
        this.authenticationTokenRepository = authenticationTokenRepository;
        this.replacedAuthTokensRepo = replacedAuthTokensRepo;
    }

    @PostMapping("/changeUsername")
    public ResponseEntity<UsernameResponse> changeUsername(@RequestBody UsernameChangeRequest request,
                                                           HttpServletResponse response) {
        try {
            // Validate the request
            if (request.getUserId() == null || request.getUsername() == null) {
                return ResponseEntity.badRequest().body(new UsernameResponse("Invalid request"));
            }

            // Find the user by ID
            Long userId = Long.parseLong(request.getUserId());
            Optional<User> userOptional = userRepository.findById(userId);
            AuthenticationToken authenticationToken = authenticationTokenRepository.findByToken(request.getAuthToken());
            if (userOptional.isPresent() && authenticationToken != null &&
                    Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
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
                authenticationToken = authenticationTokenRepository.findByUser_Id(userId);
                if (userOptional.isPresent() && authenticationToken != null &&
                        replacedAuthTokensRepo.findByReplacedToken(request.getAuthToken()) != null) {

                    Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                    long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                    cookie.setMaxAge((int) maxAgeInSeconds);
                    cookie.setPath("/"); // Save the cookie for all pages of the site
                    cookie.setSecure(true);
                    cookie.setDomain("danov-autoshow-656625355b99.herokuapp.com");

                    response.addCookie(cookie);
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
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UsernameResponse("User not found."));
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., DataAccessException)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UsernameResponse("Error changing username."));
        }
    }

    @PostMapping("/changeEmail")
    public ResponseEntity<UsernameResponse> changeEmail(@RequestBody EmailRequest request,
                                                        HttpServletResponse response) {
        String emailRegex = "[A-Za-z0-9]+[@][A-Za-z]{2,}[.][a-zA-Z]{2,}";
        try {
            // Validate the request
            if (request.getId() == null) {
                return ResponseEntity.badRequest().body(new UsernameResponse("Invalid request"));
            }

            // Find the user by ID
            Long userId = Long.parseLong(request.getId());
            Optional<User> userOptional = userRepository.findById(userId);
            AuthenticationToken authenticationToken = authenticationTokenRepository.findByToken(request.getAuthToken());
            if (userOptional.isPresent() && authenticationToken != null &&
                    Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
                if (request.getEmail().matches(emailRegex)) {
                    User user = userOptional.get();
                    if (userRepository.findByEmail(request.getEmail()) == null) {
                        user.setEmail(request.getEmail());
                        userRepository.save(user);
                        return ResponseEntity.ok(new UsernameResponse("Successfully changed the email."));
                    } else {
                        return ResponseEntity.ok(new UsernameResponse("Account with this email already exists."));
                    }
                } else {
                    return ResponseEntity.ok(new UsernameResponse("Please use a valid email."));
                }
            } else {
                authenticationToken = authenticationTokenRepository.findByUser_Id(userId);
                if (userOptional.isPresent() && authenticationToken != null &&
                        replacedAuthTokensRepo.findByReplacedToken(request.getAuthToken()) != null) {

                    Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                    long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                    cookie.setMaxAge((int) maxAgeInSeconds);
                    cookie.setPath("/"); // Save the cookie for all pages of the site
                    cookie.setSecure(true);
                    cookie.setDomain("danov-autoshow-656625355b99.herokuapp.com");

                    response.addCookie(cookie);
                    if (request.getEmail().matches(emailRegex)) {
                        User user = userOptional.get();
                        if (userRepository.findByEmail(request.getEmail()) == null) {
                            user.setEmail(request.getEmail());
                            userRepository.save(user);
                            return ResponseEntity.ok(new UsernameResponse("Successfully changed the email."));
                        } else {
                            return ResponseEntity.ok(new UsernameResponse("Account with this email already exists."));
                        }
                    } else {
                        return ResponseEntity.ok(new UsernameResponse("Please use a valid email."));
                    }
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UsernameResponse("User not found."));
            }
        } catch (
                Exception e) {
            // Handle exceptions (e.g., DataAccessException)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UsernameResponse("Error changing email."));
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<UsernameResponse> changePassword(@RequestBody PasswordRequest request,
                                                           HttpServletResponse response) {
        try {
            // Validate the request
            if (request.getId() == null) {
                return ResponseEntity.badRequest().body(new UsernameResponse("Invalid request"));
            }

            // Find the user by ID
            Long userId = Long.parseLong(request.getId());
            Optional<User> userOptional = userRepository.findById(userId);
            AuthenticationToken authenticationToken = authenticationTokenRepository.findByToken(request.getAuthToken());
            if (userOptional.isPresent() && authenticationToken != null &&
                    Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
                User user = userOptional.get();
                if (user.getPassword().equals(request.getCurrentPassword())) {
                    if (request.getNewPassword().length() >= 8) {
                        user.setPassword(request.getNewPassword());
                        userRepository.save(user);
                        return ResponseEntity.ok(new UsernameResponse("Successfully changed the password."));
                    } else {
                        return ResponseEntity.ok(new UsernameResponse("Please use password with at least 8 characters."));
                    }
                } else {
                    return ResponseEntity.ok(new UsernameResponse("Wrong password."));
                }
            } else {
                authenticationToken = authenticationTokenRepository.findByUser_Id(userId);
                if (userOptional.isPresent() && authenticationToken != null &&
                        replacedAuthTokensRepo.findByReplacedToken(request.getAuthToken()) != null) {

                    Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                    long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                    cookie.setMaxAge((int) maxAgeInSeconds);
                    cookie.setPath("/"); // Save the cookie for all pages of the site
                    cookie.setSecure(true);
                    cookie.setDomain("danov-autoshow-656625355b99.herokuapp.com");

                    response.addCookie(cookie);
                    User user = userOptional.get();
                    if (user.getPassword().equals(request.getCurrentPassword())) {
                        if (request.getNewPassword().length() >= 8) {
                            user.setPassword(request.getNewPassword());
                            userRepository.save(user);
                            return ResponseEntity.ok(new UsernameResponse("Successfully changed the password."));
                        } else {
                            return ResponseEntity.ok(new UsernameResponse("Please use password with at least 8 characters."));
                        }
                    } else {
                        return ResponseEntity.ok(new UsernameResponse("Wrong password."));
                    }
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new UsernameResponse("User not found."));
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., DataAccessException)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UsernameResponse("Error changing password."));
        }
    }
}
