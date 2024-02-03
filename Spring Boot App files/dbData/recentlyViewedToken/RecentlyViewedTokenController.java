package com.example.demo.dbData.recentlyViewedToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recently-viewed")
public class RecentlyViewedTokenController {
    private final RecentlyViewedRepository recentlyViewedRepository;

    @Autowired
    public RecentlyViewedTokenController(RecentlyViewedRepository recentlyViewedRepository) {
        this.recentlyViewedRepository = recentlyViewedRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addRecentlyViewedCar(@RequestParam String userId, String carId) {
        if (recentlyViewedRepository.findByUser_Id(Long.valueOf(userId)).isPresent()) {
            RecentlyViewedToken recentlyViewedToken = recentlyViewedRepository.findByUser_Id(Long.valueOf(userId)).get();
            String recentlyViewedCars = recentlyViewedToken.getRecentlyViewedCars();
            if (recentlyViewedCars == null || recentlyViewedCars.isEmpty()) {
                recentlyViewedCars = carId;
            } else {
                recentlyViewedCars += "," + carId;
            }
            recentlyViewedToken.setRecentlyViewedCars(recentlyViewedCars);
            recentlyViewedRepository.save(recentlyViewedToken);
            return ResponseEntity.ok("Successfully added the car." + recentlyViewedToken.getExpireDate());
        } else {
            return ResponseEntity.ok("Invalid user");
        }

    }
}
