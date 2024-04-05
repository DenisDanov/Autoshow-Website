package com.example.app.services.impl;

import com.example.app.data.DTOs.FavoriteRequestDTO;
import com.example.app.data.DTOs.FavoriteResponseDTO;
import com.example.app.data.entities.AuthenticationToken;
import com.example.app.data.entities.FavoriteVehiclesEntity;
import com.example.app.data.entities.User;
import com.example.app.services.AuthenticationTokenService;
import com.example.app.services.FavoriteVehiclesService;
import com.example.app.services.FavoritesControllerService;
import com.example.app.services.UserService;
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

    public FavoritesControllerServiceImpl(UserService userService,
                                          FavoriteVehiclesService favoriteVehiclesService,
                                          AuthenticationTokenService authenticationTokenService) {
        this.userService = userService;
        this.favoriteVehiclesService = favoriteVehiclesService;
        this.authenticationTokenService = authenticationTokenService;
    }

    @Override
    public ResponseEntity<String> addToFavorites(FavoriteRequestDTO request, HttpServletResponse response) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getUserId());
        Optional<User> userOptional = userService.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokenService.findByToken(request.getAuthToken());
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            return addVehicleToDb(request, userId, userOptional);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @Override
    public ResponseEntity<String> removeFromFavorites(FavoriteRequestDTO request, HttpServletResponse response) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getUserId());
        Optional<User> userOptional = userService.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokenService.findByToken(request.getAuthToken());
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            if (favoriteVehiclesService.deleteByVehicleIdAndUserId(request.getVehicleId(), userId) > 0 ) {
                return ResponseEntity.ok("Vehicle removed from favorites successfully.");
            } else {
                return ResponseEntity.ok("Vehicle is not present in the list.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @Override
    public ResponseEntity<List<FavoriteResponseDTO>> getFavVehicles(String id, String authToken, HttpServletResponse response) {
        Long userId = Long.parseLong(id);

        Optional<User> userOptional = userService.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokenService.findByToken(authToken);
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            List<FavoriteResponseDTO> getAllVehicles = favoriteVehiclesService
                    .findByUser_Id(userId)
                    .stream()
                    .map(favoriteVehicle -> new FavoriteResponseDTO(
                            favoriteVehicle.getVehicleId(),
                            favoriteVehicle.getVehicleImg(),
                            favoriteVehicle.getVehicleName()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(getAllVehicles);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private ResponseEntity<String> addVehicleToDb(@RequestBody FavoriteRequestDTO request, Long userId, Optional<User> userOptional) {
        User user = userOptional.get();

        List<FavoriteVehiclesEntity> matchingEntities = favoriteVehiclesService.findByUser_Id(userId).stream()
                .filter(favoriteVehiclesEntity ->
                        favoriteVehiclesEntity.getVehicleId().equals(request.getVehicleId()) &&
                                favoriteVehiclesEntity.getVehicleImg().equals(request.getVehicleImg()) &&
                                favoriteVehiclesEntity.getVehicleName().equals(request.getVehicleName()))
                .toList();
        if (matchingEntities.isEmpty()) {
            favoriteVehiclesService.save(new FavoriteVehiclesEntity(request.getVehicleId(),
                    request.getVehicleImg(), request.getVehicleName(), user));
            return ResponseEntity.ok("Vehicle added to favorites successfully.");
        } else {
            return ResponseEntity.ok("Vehicle already exists.");
        }
    }
}
