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

@RestController
@RequestMapping("/api/favorites")
public class FavoritesController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addToFavorites(@RequestBody FavoriteRequest request) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getUserId());
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Add the vehicle ID to the user's favorites
            FavoriteResponse vehicleData = new FavoriteResponse(request.getVehicleId(),
                    request.getVehicleImg(),
                    request.getVehicleName());
            if (user.getFavoriteVehicles() != null && !user.getFavoriteVehicles().contains(vehicleData)) {
                user.getFavoriteVehicles().add(vehicleData);
            } else {
                user.setFavoriteVehicles(new ArrayList<>());
            }

            // Save the updated user
            userRepository.save(user);

            return ResponseEntity.ok("Vehicle added to favorites successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeFromFavorites(@RequestBody FavoriteRequest request) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getUserId());
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Remove the vehicle ID from the user's favorites
            List<FavoriteResponse> favoriteVehicles = user.getFavoriteVehicles();

            // Modify the removal logic to check the vehicleId using equals
            if (user.getFavoriteVehicles().
                    removeIf(vehicle -> vehicle.getVehicleId().equals(request.getVehicleId()))) {
                userRepository.save(user);
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

        if (userOptional.isPresent()) {
            List<FavoriteResponse> getAllVehicles = new ArrayList<>();
            getAllVehicles.addAll(userOptional.get().getFavoriteVehicles());
            return ResponseEntity.ok(getAllVehicles);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
