package com.example.app.services.impl;

import com.example.app.data.DTOs.FavoriteRequest;
import com.example.app.data.DTOs.FavoriteResponse;
import com.example.app.data.entities.AuthenticationToken;
import com.example.app.data.entities.FavoriteVehiclesEntity;
import com.example.app.data.entities.User;
import com.example.app.services.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoritesControllerServiceImpl implements FavoritesControllerService {

    private final UserService userService;

    private final FavoriteVehiclesService favoriteVehiclesService;

    private final AuthenticationTokenService authenticationTokenService;

    private final ReplacedAuthTokensService replacedAuthTokensService;

    public FavoritesControllerServiceImpl(UserService userService,
                                          FavoriteVehiclesService favoriteVehiclesService,
                                          AuthenticationTokenService authenticationTokenService,
                                          ReplacedAuthTokensService replacedAuthTokensService) {
        this.userService = userService;
        this.favoriteVehiclesService = favoriteVehiclesService;
        this.authenticationTokenService = authenticationTokenService;
        this.replacedAuthTokensService = replacedAuthTokensService;
    }

    @Override
    public ResponseEntity<String> addToFavorites(FavoriteRequest request, HttpServletResponse response) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getUserId());
        Optional<User> userOptional = userService.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokenService.findByToken(request.getAuthToken());
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            return addVehicleToDb(request, userId, userOptional);
        } else {
            authenticationToken = authenticationTokenService.findByUser_Id(userId);
            if (userOptional.isPresent() && authenticationToken != null &&
                    replacedAuthTokensService.findByReplacedToken(request.getAuthToken()) != null) {

                Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                cookie.setMaxAge((int) maxAgeInSeconds);
                cookie.setPath("/"); // Save the cookie for all pages of the site

                response.addCookie(cookie);
                return addVehicleToDb(request, userId, userOptional);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @Override
    public ResponseEntity<String> removeFromFavorites(FavoriteRequest request, HttpServletResponse response) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getUserId());
        Optional<User> userOptional = userService.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokenService.findByToken(request.getAuthToken());
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            //removal logic
            if (request.getVehicleId().contains("car=")) {
                request.setVehicleId(request.getVehicleId().split("car=")[1].replaceAll("%20", " "));
            }
            if (favoriteVehiclesService.deleteByVehicleIdAndUserId(request.getVehicleId(), userId) > 0) {
                return ResponseEntity.ok("Vehicle removed from favorites successfully.");
            } else {
                return ResponseEntity.ok("Vehicle is not present in the list.");
            }
        } else {
            authenticationToken = authenticationTokenService.findByUser_Id(userId);
            if (userOptional.isPresent() && authenticationToken != null &&
                    replacedAuthTokensService.findByReplacedToken(request.getAuthToken()) != null) {

                Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                cookie.setMaxAge((int) maxAgeInSeconds);
                cookie.setPath("/"); // Save the cookie for all pages of the site

                response.addCookie(cookie);
                if (favoriteVehiclesService.deleteByVehicleIdAndUserId(request.getVehicleId(), userId) > 0) {
                    return ResponseEntity.ok("Vehicle removed from favorites successfully.");
                } else {
                    return ResponseEntity.ok("Vehicle is not present in the list.");
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @Override
    public ResponseEntity<List<FavoriteResponse>> getFavVehicles(String id, String authToken, HttpServletResponse response) {
        Long userId = Long.parseLong(id);

        Optional<User> userOptional = userService.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokenService.findByToken(authToken);
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            List<FavoriteResponse> getAllVehicles = favoriteVehiclesService
                    .findByUser_Id(userId)
                    .stream()
                    .map(favoriteVehicle -> new FavoriteResponse(
                            favoriteVehicle.getVehicleId(),
                            favoriteVehicle.getVehicleImg(),
                            favoriteVehicle.getVehicleName()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(getAllVehicles);
        } else {
            authenticationToken = authenticationTokenService.findByUser_Id(userId);
            if (userOptional.isPresent() && authenticationToken != null &&
                    replacedAuthTokensService.findByReplacedToken(authToken) != null) {

                Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                cookie.setMaxAge((int) maxAgeInSeconds);
                cookie.setPath("/"); // Save the cookie for all pages of the site

                response.addCookie(cookie);
                List<FavoriteResponse> getAllVehicles = favoriteVehiclesService
                        .findByUser_Id(userId)
                        .stream()
                        .map(favoriteVehicle -> new FavoriteResponse(
                                favoriteVehicle.getVehicleId(),
                                favoriteVehicle.getVehicleImg(),
                                favoriteVehicle.getVehicleName()))
                        .collect(Collectors.toList());

                return ResponseEntity.ok(getAllVehicles);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private ResponseEntity<String> addVehicleToDb(@RequestBody FavoriteRequest request, Long userId, Optional<User> userOptional) {
        User user = userOptional.get();

        List<FavoriteVehiclesEntity> matchingEntities = favoriteVehiclesService.findByUser_Id(userId).stream()
                .filter(favoriteVehiclesEntity ->
                        favoriteVehiclesEntity.getVehicleId().equals(request.getVehicleId()) &&
                                favoriteVehiclesEntity.getVehicleImg().equals(request.getVehicleImg()) &&
                                favoriteVehiclesEntity.getVehicleName().equals(request.getVehicleName()))
                .collect(Collectors.toList());

        if (matchingEntities.isEmpty()) {
            if (request.getVehicleId().contains("car=")) {
                request.setVehicleId(request.getVehicleId().split("car=")[1].replaceAll("%20", " "));
            }
            favoriteVehiclesService.save(new FavoriteVehiclesEntity(request.getVehicleId(),
                    request.getVehicleImg(), request.getVehicleName(), user));
            return ResponseEntity.ok("Vehicle added to favorites successfully.");
        } else {
            return ResponseEntity.ok("Vehicle already exists.");
        }
    }
}
