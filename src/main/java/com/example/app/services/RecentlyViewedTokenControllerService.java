package com.example.app.services;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface RecentlyViewedTokenControllerService {

    ResponseEntity<String> addRecentlyViewedCar(@RequestParam String userId, String carId, String authToken,
                                                HttpServletResponse response);

    ResponseEntity<List<String>> getRecentlyViewedCars(@RequestParam String userId, String authToken,
                                                       HttpServletResponse response);
}
