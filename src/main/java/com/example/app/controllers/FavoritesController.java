package com.example.app.controllers;

import com.example.app.data.DTOs.FavoriteRequestDTO;
import com.example.app.data.DTOs.FavoriteResponseDTO;
import com.example.app.services.FavoritesControllerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoritesController {

    private final FavoritesControllerService favoritesControllerService;

    public FavoritesController(FavoritesControllerService favoritesControllerService) {
        this.favoritesControllerService = favoritesControllerService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToFavorites(@RequestBody FavoriteRequestDTO request,
                                                 HttpServletResponse response) {
        return favoritesControllerService.addToFavorites(request, response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromFavorites(@RequestBody FavoriteRequestDTO request,
                                                      HttpServletResponse response) {
        return favoritesControllerService.removeFromFavorites(request,response);
    }

    @GetMapping("/get")
    public ResponseEntity<List<FavoriteResponseDTO>> getFavVehicles(@RequestParam String id, String authToken,
                                                                    HttpServletResponse response) {
        return favoritesControllerService.getFavVehicles(id,authToken,response);
    }

}
