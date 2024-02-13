package com.example.demo.dbData.recentlyViewedToken;

import com.example.demo.dbData.*;
import com.example.demo.dbData.ReplacedTokens.ReplacedAuthTokensRepo;
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

    private final AuthenticationTokensRepository authenticationTokensRepository;

    private final ReplacedAuthTokensRepo replacedAuthTokensRepo;

    @Autowired
    public RecentlyViewedTokenController(RecentlyViewedRepository recentlyViewedRepository,
                                         UserRepository userRepository,
                                         AuthenticationTokensRepository authenticationTokensRepository,
                                         ReplacedAuthTokensRepo replacedAuthTokensRepo) {
        this.recentlyViewedRepository = recentlyViewedRepository;
        this.userRepository = userRepository;
        this.authenticationTokensRepository = authenticationTokensRepository;
        this.replacedAuthTokensRepo = replacedAuthTokensRepo;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addRecentlyViewedCar(@RequestParam String userId, String carId, String authToken,
                                                       HttpServletResponse response) {
        AuthenticationToken authenticationToken = authenticationTokensRepository.findByToken(authToken);
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
            authenticationToken = authenticationTokensRepository.findByUser_Id(Long.valueOf(userId));
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
                replacedAuthTokensRepo.deleteByReplacedToken(authToken);
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
}
