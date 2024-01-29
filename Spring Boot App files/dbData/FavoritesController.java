package com.example.demo.dbData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
public class FavoritesController {

    private final UserRepository userRepository;

    private final FavoriteVehiclesRepository favoriteVehiclesRepository;

    private final  AuthenticationTokensRepository authenticationTokensRepository;

    @Autowired
    public FavoritesController(UserRepository userRepository,
                               FavoriteVehiclesRepository favoriteVehiclesRepository,
                               AuthenticationTokensRepository authenticationTokensRepository) {
        this.userRepository = userRepository;
        this.favoriteVehiclesRepository = favoriteVehiclesRepository;
        this.authenticationTokensRepository = authenticationTokensRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToFavorites(@RequestBody FavoriteRequest request) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getUserId());
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent() &&
                authenticationTokensRepository.findByToken(request.getAuthToken()) != null) {
            User user = userOptional.get();
            // Add the vehicle ID to the user's favorites
            FavoriteResponse vehicleData = new FavoriteResponse(request.getVehicleId(),
                    request.getVehicleImg(),
                    request.getVehicleName());

            List<FavoriteVehiclesEntity> matchingEntities = favoriteVehiclesRepository.findByUser_Id(userId).stream()
                    .filter(favoriteVehiclesEntity ->
                            favoriteVehiclesEntity.getVehicleId().equals(request.getVehicleId()) &&
                                    favoriteVehiclesEntity.getVehicleImg().equals(request.getVehicleImg()) &&
                                    favoriteVehiclesEntity.getVehicleName().equals(request.getVehicleName()))
                    .collect(Collectors.toList());

            if (matchingEntities.isEmpty()) {
                favoriteVehiclesRepository.save(new FavoriteVehiclesEntity(request.getVehicleId(),
                        request.getVehicleImg(), request.getVehicleName(), user));
                return ResponseEntity.ok("Vehicle added to favorites successfully.");
            } else {
                return ResponseEntity.ok("Vehicle already exists.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeFromFavorites(@RequestBody FavoriteRequest request) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getUserId());
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent() &&
                authenticationTokensRepository.findByToken(request.getAuthToken()) != null) {
            User user = userOptional.get();
            // Remove the vehicle ID from the user's favorites
            List<FavoriteVehiclesEntity> favoriteVehicles = favoriteVehiclesRepository.findByUser_Id(userId);

            // Modify the removal logic to check the vehicleId using equals
            if (favoriteVehiclesRepository.deleteByVehicleIdAndUserId(request.getVehicleId(), userId) > 0) {
                return ResponseEntity.ok("Vehicle removed from favorites successfully.");
            } else {
                return ResponseEntity.ok("Vehicle is not present in the list.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @PostMapping("/get")
    public ResponseEntity<List<FavoriteResponse>> getFavVehicles(@RequestBody FavoriteRequest request) {
        Long userId = Long.parseLong(request.getUserId());

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent() &&
                authenticationTokensRepository.findByToken(request.getAuthToken()) != null) {
            List<FavoriteResponse> getAllVehicles = favoriteVehiclesRepository
                    .findByUser_Id(userId)
                    .stream()
                    .map(favoriteVehicle -> new FavoriteResponse(
                            favoriteVehicle.getVehicleId(),
                            favoriteVehicle.getVehicleImg(),
                            favoriteVehicle.getVehicleName()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(getAllVehicles);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
