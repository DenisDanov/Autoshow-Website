package com.example.demo.dbData.recentlyViewedToken;

import com.example.demo.dbData.AuthenticationToken;
import com.example.demo.dbData.AuthenticationTokensRepository;
import com.example.demo.dbData.User;
import com.example.demo.dbData.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/recently-viewed")
public class RecentlyViewedTokenController {
    private final RecentlyViewedRepository recentlyViewedRepository;

    private final UserRepository userRepository;

    private final AuthenticationTokensRepository authenticationTokensRepository;

    @Autowired
    public RecentlyViewedTokenController(RecentlyViewedRepository recentlyViewedRepository,
                                         UserRepository userRepository,
                                         AuthenticationTokensRepository authenticationTokensRepository) {
        this.recentlyViewedRepository = recentlyViewedRepository;
        this.userRepository = userRepository;
        this.authenticationTokensRepository = authenticationTokensRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addRecentlyViewedCar(@RequestParam String userId, String carId, String authToken) {
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
                if (listCars.contains(carId)) {
                    listCars.remove(carId);
                }
                listCars.add(carId);
                recentlyViewedCars = String.join(",", listCars);
            }
            recentlyViewedToken.setRecentlyViewedCars(recentlyViewedCars);
            recentlyViewedRepository.save(recentlyViewedToken);
            return ResponseEntity.ok("Successfully added the car." + recentlyViewedToken.getExpireDate());
        } else {
            return ResponseEntity.ok("Invalid user");
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Date> getRecentlyViewedTokenExpireDate(@RequestParam String userId, String authToken) {
        AuthenticationToken authenticationToken = authenticationTokensRepository.findByToken(authToken);
        if (userRepository.findById(Long.parseLong(userId)).isPresent()) {
            User user = userRepository.findById(Long.parseLong(userId)).get();
            if (authenticationToken != null &&
                    Objects.equals(authenticationToken.getUser().getId(), user.getId())) {
                if (recentlyViewedRepository.findByUser_Id(user.getId()).isPresent()) {
                    return ResponseEntity.ok(recentlyViewedRepository.findByUser_Id(user.getId()).get().getExpireDate());
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
