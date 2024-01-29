package com.example.demo.dbData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profile")
public class ProfileDataController {

    private UserRepository userRepository;

    private FavoriteVehiclesRepository favoriteVehiclesRepository;

    private AuthenticationTokensRepository authenticationTokensRepository;

    @Autowired
    public ProfileDataController(UserRepository userRepository,
                                 FavoriteVehiclesRepository favoriteVehiclesRepository,
                                 AuthenticationTokensRepository authenticationTokensRepository) {
        this.userRepository = userRepository;
        this.favoriteVehiclesRepository = favoriteVehiclesRepository;
        this.authenticationTokensRepository = authenticationTokensRepository;
    }

    @PostMapping("/get")
    public ResponseEntity<ProfileResponse> getProfileData(@RequestBody ProfileRequest request) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getUserId());
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent() &&
                authenticationTokensRepository.findByToken(request.getAuthToken()) != null) {
            User user = userOptional.get();
            List<FavoriteResponse> getAllVehicles = favoriteVehiclesRepository
                    .findByUser_Id(userId)
                    .stream()
                    .map(favoriteVehicle -> new FavoriteResponse(
                            favoriteVehicle.getVehicleId(),
                            favoriteVehicle.getVehicleImg(),
                            favoriteVehicle.getVehicleName()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ProfileResponse(user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    getAllVehicles));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
