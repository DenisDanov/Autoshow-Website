package com.example.demo.dbData;

import com.example.demo.dbData.ReplacedTokens.ReplacedAuthTokensRepo;
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
@RequestMapping("/api/favorites")
public class FavoritesController {

    private final UserRepository userRepository;

    private final FavoriteVehiclesRepository favoriteVehiclesRepository;

    private final AuthenticationTokenRepository authenticationTokenRepository;

    private final ReplacedAuthTokensRepo replacedAuthTokensRepo;

    @Autowired
    public FavoritesController(UserRepository userRepository,
                               FavoriteVehiclesRepository favoriteVehiclesRepository,
                               AuthenticationTokenRepository authenticationTokenRepository,
                               ReplacedAuthTokensRepo replacedAuthTokensRepo) {
        this.userRepository = userRepository;
        this.favoriteVehiclesRepository = favoriteVehiclesRepository;
        this.authenticationTokenRepository = authenticationTokenRepository;
        this.replacedAuthTokensRepo = replacedAuthTokensRepo;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToFavorites(@RequestBody FavoriteRequest request,
                                                 HttpServletResponse response) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getUserId());
        Optional<User> userOptional = userRepository.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokenRepository.findByToken(request.getAuthToken());
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            return addVehicleToDb(request,userId,userOptional);
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
                replacedAuthTokensRepo.deleteByReplacedToken(request.getAuthToken());
                return addVehicleToDb(request,userId,userOptional);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    private ResponseEntity<String> addVehicleToDb(@RequestBody FavoriteRequest request, Long userId, Optional<User> userOptional) {
        User user = userOptional.get();

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
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromFavorites(@RequestBody FavoriteRequest request,
                                                      HttpServletResponse response) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getUserId());
        Optional<User> userOptional = userRepository.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokenRepository.findByToken(request.getAuthToken());
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            //removal logic
            if (favoriteVehiclesRepository.deleteByVehicleIdAndUserId(request.getVehicleId(), userId) > 0) {
                return ResponseEntity.ok("Vehicle removed from favorites successfully.");
            } else {
                return ResponseEntity.ok("Vehicle is not present in the list.");
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
                replacedAuthTokensRepo.deleteByReplacedToken(request.getAuthToken());
                if (favoriteVehiclesRepository.deleteByVehicleIdAndUserId(request.getVehicleId(), userId) > 0) {
                    return ResponseEntity.ok("Vehicle removed from favorites successfully.");
                } else {
                    return ResponseEntity.ok("Vehicle is not present in the list.");
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<FavoriteResponse>> getFavVehicles(@RequestParam String id, String authToken,
                                                                 HttpServletResponse response) {
        Long userId = Long.parseLong(id);

        Optional<User> userOptional = userRepository.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokenRepository.findByToken(authToken);
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
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
            authenticationToken = authenticationTokenRepository.findByUser_Id(userId);
            if (userOptional.isPresent() && authenticationToken != null &&
                    replacedAuthTokensRepo.findByReplacedToken(authToken) != null) {

                Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                cookie.setMaxAge((int) maxAgeInSeconds);
                cookie.setPath("/"); // Save the cookie for all pages of the site
                cookie.setSecure(true);
                cookie.setDomain("danov-autoshow-656625355b99.herokuapp.com");

                response.addCookie(cookie);
                replacedAuthTokensRepo.deleteByReplacedToken(authToken);
                List<FavoriteResponse> getAllVehicles = favoriteVehiclesRepository
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

}
