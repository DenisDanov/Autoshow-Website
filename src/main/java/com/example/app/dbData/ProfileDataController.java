package com.example.app.dbData;

import com.example.app.dbData.ReplacedTokens.ReplacedAuthTokensRepo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/profile")
public class ProfileDataController {

    private final UserRepository userRepository;

    private final FavoriteVehiclesRepository favoriteVehiclesRepository;

    private final AuthenticationTokenRepository authenticationTokenRepository;

    private final ReplacedAuthTokensRepo replacedAuthTokensRepo;

    @Autowired
    public ProfileDataController(UserRepository userRepository,
                                 FavoriteVehiclesRepository favoriteVehiclesRepository,
                                 AuthenticationTokenRepository authenticationTokenRepository,
                                 ReplacedAuthTokensRepo replacedAuthTokensRepo) {
        this.userRepository = userRepository;
        this.favoriteVehiclesRepository = favoriteVehiclesRepository;
        this.authenticationTokenRepository = authenticationTokenRepository;
        this.replacedAuthTokensRepo = replacedAuthTokensRepo;
    }

    @GetMapping("/get")
    public ResponseEntity<ProfileResponse> getProfileData(@RequestParam String id, String authToken,
                                                          HttpServletResponse response) {
        // Find the user by ID
        Long userId = Long.parseLong(id);
        Optional<User> userOptional = userRepository.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokenRepository.findByToken(authToken);
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
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
                    getAllVehicles));
        } else {
            authenticationToken = authenticationTokenRepository.findByUser_Id(userId);
            if (userOptional.isPresent() && authenticationToken != null &&
            replacedAuthTokensRepo.findByReplacedToken(authToken) != null) {
                User user = userOptional.get();
                List<FavoriteResponse> getAllVehicles = favoriteVehiclesRepository
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