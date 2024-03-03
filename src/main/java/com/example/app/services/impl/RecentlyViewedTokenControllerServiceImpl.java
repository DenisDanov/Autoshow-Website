package com.example.app.services.impl;

import com.example.app.data.entities.AuthenticationToken;
import com.example.app.data.entities.RecentlyViewedToken;
import com.example.app.data.entities.User;
import com.example.app.services.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RecentlyViewedTokenControllerServiceImpl implements RecentlyViewedTokenControllerService {

    private final RecentlyViewedTokenService recentlyViewedTokenService;

    private final UserService userService;

    private final AuthenticationTokenService authenticationTokenService;

    private final ReplacedAuthTokensService replacedAuthTokensService;

    private final DeleteReplacedTokensService deletionService;

    public RecentlyViewedTokenControllerServiceImpl(RecentlyViewedTokenService recentlyViewedTokenService,
                                                    UserService userService,
                                                    AuthenticationTokenService authenticationTokenService,
                                                    ReplacedAuthTokensService replacedAuthTokensService,
                                                    DeleteReplacedTokensService deletionService) {
        this.recentlyViewedTokenService = recentlyViewedTokenService;
        this.userService = userService;
        this.authenticationTokenService = authenticationTokenService;
        this.replacedAuthTokensService = replacedAuthTokensService;
        this.deletionService = deletionService;
    }

    @Override
    public ResponseEntity<String> addRecentlyViewedCar(String userId, String carId, String authToken, HttpServletResponse response) {
        AuthenticationToken authenticationToken = authenticationTokenService.findByToken(authToken);
        if (recentlyViewedTokenService.findByUser_Id(Long.valueOf(userId)).isPresent() &&
                authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), Long.valueOf(userId))) {
            RecentlyViewedToken recentlyViewedToken = recentlyViewedTokenService.findByUser_Id(Long.valueOf(userId)).get();
            String recentlyViewedCars = recentlyViewedToken.getRecentlyViewedCars();
            if (recentlyViewedCars == null || recentlyViewedCars.isEmpty()) {
                recentlyViewedCars = carId;
            } else {
                List<String> listCars = new ArrayList<>(List.of(recentlyViewedCars.split(",")));
                listCars.remove(carId);
                listCars.add(carId);
                recentlyViewedCars = String.join(",", listCars);
            }
            recentlyViewedToken.setRecentlyViewedCars(recentlyViewedCars);
            recentlyViewedTokenService.save(recentlyViewedToken);
            return ResponseEntity.ok("Successfully added the car." + recentlyViewedToken.getExpireDate());
        } else {
            authenticationToken = authenticationTokenService.findByUser_Id(Long.valueOf(userId));
            Optional<User> userOptional = userService.findById(Long.parseLong(userId));
            if (userOptional.isPresent() && authenticationToken != null &&
                    replacedAuthTokensService.findByReplacedToken(authToken) != null) {

                Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                cookie.setMaxAge((int) maxAgeInSeconds);
                cookie.setPath("/"); // Save the cookie for all pages of the site
                cookie.setSecure(true);
                cookie.setDomain("danov-autoshow.azurewebsites.net");

                response.addCookie(cookie);
                deletionService.scheduleDeletion(authToken);

                RecentlyViewedToken recentlyViewedToken = recentlyViewedTokenService.findByUser_Id(Long.valueOf(userId)).get();
                String recentlyViewedCars = recentlyViewedToken.getRecentlyViewedCars();
                if (recentlyViewedCars == null || recentlyViewedCars.isEmpty()) {
                    recentlyViewedCars = carId;
                } else {
                    List<String> listCars = new ArrayList<>(List.of(recentlyViewedCars.split(",")));
                    listCars.remove(carId);
                    listCars.add(carId);
                    recentlyViewedCars = String.join(",", listCars);
                }
                recentlyViewedToken.setRecentlyViewedCars(recentlyViewedCars);
                recentlyViewedTokenService.save(recentlyViewedToken);
                return ResponseEntity.ok("Successfully added the car." + recentlyViewedToken.getExpireDate());
            }
            return ResponseEntity.ok("Invalid user");
        }
    }

    @Override
    public ResponseEntity<List<String>> getRecentlyViewedCars(String userId, String authToken, HttpServletResponse response) {
        AuthenticationToken authenticationToken = authenticationTokenService.findByToken(authToken);

        if (userService.findById(Long.parseLong(userId)).isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), Long.valueOf(userId))) {

            if (recentlyViewedTokenService.findByUser_Id(Long.valueOf(userId)).isPresent()) {
                RecentlyViewedToken recentlyViewedToken = recentlyViewedTokenService.findByUser_Id(Long.valueOf(userId)).get();
                if (recentlyViewedToken.getRecentlyViewedCars() != null &&
                        !recentlyViewedToken.getRecentlyViewedCars().isEmpty()) {
                    List<String> recentlyViewedCars = List.of(recentlyViewedToken.
                            getRecentlyViewedCars().split(","));
                    return ResponseEntity.ok(recentlyViewedCars);
                } else {
                    return ResponseEntity.ok(new ArrayList<>());
                }
            } else {
                return ResponseEntity.ok(new ArrayList<>());
            }

        } else {
            authenticationToken = authenticationTokenService.findByUser_Id(Long.valueOf(userId));
            Optional<User> userOptional = userService.findById(Long.parseLong(userId));
            if (userOptional.isPresent() && authenticationToken != null &&
                    replacedAuthTokensService.findByReplacedToken(authToken) != null) {

                Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                cookie.setMaxAge((int) maxAgeInSeconds);
                cookie.setPath("/"); // Save the cookie for all pages of the site
                cookie.setSecure(true);
                cookie.setDomain("danov-autoshow.azurewebsites.net");

                response.addCookie(cookie);

                if (recentlyViewedTokenService.findByUser_Id(Long.valueOf(userId)).isPresent()) {
                    RecentlyViewedToken recentlyViewedToken = recentlyViewedTokenService.findByUser_Id(Long.valueOf(userId)).get();
                    if (recentlyViewedToken.getRecentlyViewedCars() != null &&
                            !recentlyViewedToken.getRecentlyViewedCars().isEmpty()) {
                        List<String> recentlyViewedCars = List.of(recentlyViewedToken.
                                getRecentlyViewedCars().split(","));
                        return ResponseEntity.ok(recentlyViewedCars);
                    } else {
                        return ResponseEntity.ok(new ArrayList<>());
                    }
                } else {
                    return ResponseEntity.ok(new ArrayList<>());
                }
            }
            return ResponseEntity.ok(new ArrayList<>());
        }
    }
}
