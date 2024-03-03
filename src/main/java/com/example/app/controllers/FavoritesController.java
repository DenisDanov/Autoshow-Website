package com.example.app.controllers;

import com.example.app.data.DTOs.FavoriteRequest;
import com.example.app.data.DTOs.FavoriteResponse;
import com.example.app.data.entities.AuthenticationToken;
import com.example.app.data.entities.FavoriteVehiclesEntity;
import com.example.app.data.entities.User;
import com.example.app.services.FavoritesControllerService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    private final FavoritesControllerService favoritesControllerService;

    public FavoritesController(FavoritesControllerService favoritesControllerService) {
        this.favoritesControllerService = favoritesControllerService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToFavorites(@RequestBody FavoriteRequest request,
                                                 HttpServletResponse response) {
        return favoritesControllerService.addToFavorites(request, response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromFavorites(@RequestBody FavoriteRequest request,
                                                      HttpServletResponse response) {
        return favoritesControllerService.removeFromFavorites(request,response);
    }

    @GetMapping("/get")
    public ResponseEntity<List<FavoriteResponse>> getFavVehicles(@RequestParam String id, String authToken,
                                                                 HttpServletResponse response) {
        return favoritesControllerService.getFavVehicles(id,authToken,response);
    }

}
