package com.example.demo.dbData.recentlyViewedToken;

import com.example.demo.dbData.AuthenticationToken;
import com.example.demo.dbData.AuthenticationTokenRepository;
import com.example.demo.dbData.ReplacedTokens.DeleteReplacedTokensService;
import com.example.demo.dbData.ReplacedTokens.ReplacedAuthTokensRepo;
import com.example.demo.dbData.User;
import com.example.demo.dbData.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/recently-viewed")
public class RecentlyViewedTokenController {
    private final RecentlyViewedRepository recentlyViewedRepository;

    private final UserRepository userRepository;

    private final AuthenticationTokenRepository authenticationTokenRepository;

    private final ReplacedAuthTokensRepo replacedAuthTokensRepo;

    private final DeleteReplacedTokensService deletionService;

    @Autowired
    public RecentlyViewedTokenController(RecentlyViewedRepository recentlyViewedRepository,
                                         UserRepository userRepository,
                                         AuthenticationTokenRepository authenticationTokenRepository,
                                         ReplacedAuthTokensRepo replacedAuthTokensRepo,
                                         DeleteReplacedTokensService deletionService) {
        this.recentlyViewedRepository = recentlyViewedRepository;
        this.userRepository = userRepository;
        this.authenticationTokenRepository = authenticationTokenRepository;
        this.replacedAuthTokensRepo = replacedAuthTokensRepo;
        this.deletionService = deletionService;
    }

    @PostMapping("/add")
    private ResponseEntity<String> addRecentlyViewedCar(@RequestParam String userId, String carId, String authToken,
                                                        HttpServletResponse response) {
        AuthenticationToken authenticationToken = authenticationTokenRepository.findByToken(authToken);
        if (recentlyViewedRepository.findByUser_Id(Long.valueOf(userId)).isPresent() &&
                authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), Long.valueOf(userId))) {
            RecentlyViewedToken recentlyViewedToken = recentlyViewedRepository.findByUser_Id(Long.valueOf(userId)).get();
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
            recentlyViewedRepository.save(recentlyViewedToken);
            return ResponseEntity.ok("Successfully added the car." + recentlyViewedToken.getExpireDate());
        } else {
            authenticationToken = authenticationTokenRepository.findByUser_Id(Long.valueOf(userId));
            Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
            if (userOptional.isPresent() && authenticationToken != null &&
                    replacedAuthTokensRepo.findByReplacedToken(authToken) != null) {

                Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                cookie.setMaxAge((int) maxAgeInSeconds);
                cookie.setPath("/"); // Save the cookie for all pages of the site
                cookie.setSecure(true);
                cookie.setDomain("danov-autoshow-656625355b99.herokuapp.com");

                response.addCookie(cookie);
                deletionService.scheduleDeletion(replacedAuthTokensRepo,authToken);

                RecentlyViewedToken recentlyViewedToken = recentlyViewedRepository.findByUser_Id(Long.valueOf(userId)).get();
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
                recentlyViewedRepository.save(recentlyViewedToken);
                return ResponseEntity.ok("Successfully added the car." + recentlyViewedToken.getExpireDate());
            }
            return ResponseEntity.ok("Invalid user");
        }
    }

    @GetMapping("/get")
    private ResponseEntity<List<String>> getRecentlyViewedCars(@RequestParam String userId, String authToken,
                                                               HttpServletResponse response) {
        AuthenticationToken authenticationToken = authenticationTokenRepository.findByToken(authToken);

        if (userRepository.findById(Long.parseLong(userId)).isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), Long.valueOf(userId))) {

            if (recentlyViewedRepository.findByUser_Id(Long.valueOf(userId)).isPresent()) {
                RecentlyViewedToken recentlyViewedToken = recentlyViewedRepository.findByUser_Id(Long.valueOf(userId)).get();
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
            authenticationToken = authenticationTokenRepository.findByUser_Id(Long.valueOf(userId));
            Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));
            if (userOptional.isPresent() && authenticationToken != null &&
                    replacedAuthTokensRepo.findByReplacedToken(authToken) != null) {

                Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                cookie.setMaxAge((int) maxAgeInSeconds);
                cookie.setPath("/"); // Save the cookie for all pages of the site
                cookie.setSecure(true);
                cookie.setDomain("danov-autoshow-656625355b99.herokuapp.com");

                response.addCookie(cookie);

                if (recentlyViewedRepository.findByUser_Id(Long.valueOf(userId)).isPresent()) {
                    RecentlyViewedToken recentlyViewedToken = recentlyViewedRepository.findByUser_Id(Long.valueOf(userId)).get();
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
