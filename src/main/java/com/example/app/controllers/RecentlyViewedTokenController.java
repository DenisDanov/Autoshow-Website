package com.example.app.controllers;

import com.example.app.services.RecentlyViewedTokenControllerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recently-viewed")
public class RecentlyViewedTokenController {

    private final RecentlyViewedTokenControllerService recentlyViewedTokenControllerService;

    public RecentlyViewedTokenController(RecentlyViewedTokenControllerService recentlyViewedTokenControllerService) {
        this.recentlyViewedTokenControllerService = recentlyViewedTokenControllerService;
    }

    @PostMapping("/add")
    private ResponseEntity<String> addRecentlyViewedCar(@RequestParam String userId, String carId, String authToken,
                                                        HttpServletResponse response) {
        return this.recentlyViewedTokenControllerService.addRecentlyViewedCar(
                userId,
                carId,
                authToken,
                response
        );
    }

    @GetMapping("/get")
    private ResponseEntity<List<String>> getRecentlyViewedCars(@RequestParam String userId, String authToken,
                                                               HttpServletResponse response) {
        return this.recentlyViewedTokenControllerService.getRecentlyViewedCars(userId, authToken, response);
    }
}
