package com.example.app.services.impl;

import com.example.app.data.DTOs.FavoriteResponse;
import com.example.app.data.DTOs.ProfileResponse;
import com.example.app.data.entities.AuthenticationToken;
import com.example.app.data.entities.User;
import com.example.app.services.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileDataControllerServiceImpl implements ProfileDataControllerService {

    private final UserService userService;

    private final FavoriteVehiclesService favoriteVehiclesService;

    private final AuthenticationTokenService authenticationTokenService;

    private final ReplacedAuthTokensService replacedAuthTokensService;

    public ProfileDataControllerServiceImpl(UserService userService,
                                            FavoriteVehiclesService favoriteVehiclesService,
                                            AuthenticationTokenService authenticationTokenService,
                                            ReplacedAuthTokensService replacedAuthTokensService) {
        this.userService = userService;
        this.favoriteVehiclesService = favoriteVehiclesService;
        this.authenticationTokenService = authenticationTokenService;
        this.replacedAuthTokensService = replacedAuthTokensService;
    }

    @Override
    public ResponseEntity<ProfileResponse> getProfileData(String id, String authToken, HttpServletResponse response) {
        // Find the user by ID
        Long userId = Long.parseLong(id);
        Optional<User> userOptional = userService.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokenService.findByToken(authToken);
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            User user = userOptional.get();
            List<FavoriteResponse> getAllVehicles = favoriteVehiclesService
                    .findByUser_Id(userId)
                    .stream()
                    .map(favoriteVehicle -> new FavoriteResponse(
                            favoriteVehicle.getVehicleId(),
                            favoriteVehicle.getVehicleImg(),
                            favoriteVehicle.getVehicleName()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ProfileResponse(user.getUsername(),
                    user.getEmail(),
                    getAllVehicles));
        } else {
            authenticationToken = authenticationTokenService.findByUser_Id(userId);
            if (userOptional.isPresent() && authenticationToken != null &&
                    replacedAuthTokensService.findByReplacedToken(authToken) != null) {
                User user = userOptional.get();
                List<FavoriteResponse> getAllVehicles = favoriteVehiclesService
                        .findByUser_Id(userId)
                        .stream()
                        .map(favoriteVehicle -> new FavoriteResponse(
                                favoriteVehicle.getVehicleId(),
                                favoriteVehicle.getVehicleImg(),
                                favoriteVehicle.getVehicleName()))
                        .collect(Collectors.toList());

                Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                cookie.setMaxAge((int) maxAgeInSeconds);
                cookie.setPath("/"); // Save the cookie for all pages of the site
                cookie.setSecure(true);
                cookie.setDomain("danov-autoshow.azurewebsites.net");

                response.addCookie(cookie);
                return ResponseEntity.ok(new ProfileResponse(user.getUsername(),
                        user.getEmail(),
                        getAllVehicles));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
