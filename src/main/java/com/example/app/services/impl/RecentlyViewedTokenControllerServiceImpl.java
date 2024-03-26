package com.example.app.services.impl;

import com.example.app.data.entities.AuthenticationToken;
import com.example.app.data.entities.RecentlyViewedToken;
import com.example.app.services.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class RecentlyViewedTokenControllerServiceImpl implements RecentlyViewedTokenControllerService {

    private final RecentlyViewedTokenService recentlyViewedTokenService;

    private final UserService userService;

    private final AuthenticationTokenService authenticationTokenService;

    public RecentlyViewedTokenControllerServiceImpl(RecentlyViewedTokenService recentlyViewedTokenService,
                                                    UserService userService,
                                                    AuthenticationTokenService authenticationTokenService) {
        this.recentlyViewedTokenService = recentlyViewedTokenService;
        this.userService = userService;
        this.authenticationTokenService = authenticationTokenService;
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
        } else if (userService.findById(Long.valueOf(userId)).isPresent() &&
                authenticationToken != null &&
                authenticationToken.getUser().getId() == Long.valueOf(userId)) {

            RecentlyViewedToken recentlyViewedToken = new RecentlyViewedToken();
            recentlyViewedToken.setExpireDate();
            recentlyViewedToken.setUser(userService.findById(Long.valueOf(userId)).get());
            recentlyViewedToken.setRecentlyViewedCars(carId);

            recentlyViewedTokenService.save(recentlyViewedToken);
            return ResponseEntity.ok("Successfully added the car." + recentlyViewedToken.getExpireDate());
        }
        return ResponseEntity.ok("Invalid user");
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
                }
            }
        }
        return ResponseEntity.ok(new ArrayList<>());
    }
}
