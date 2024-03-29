package com.example.app.services;

import com.example.app.data.DTOs.FavoriteRequestDTO;
import com.example.app.data.DTOs.FavoriteResponseDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface FavoritesControllerService {

    ResponseEntity<String> addToFavorites(@RequestBody FavoriteRequestDTO request,
                                          HttpServletResponse response);

    ResponseEntity<String> removeFromFavorites(@RequestBody FavoriteRequestDTO request,
                                               HttpServletResponse response);

    ResponseEntity<List<FavoriteResponseDTO>> getFavVehicles(@RequestParam String id, String authToken,
                                                             HttpServletResponse response);
}
