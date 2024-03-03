package com.example.app.services;

import com.example.app.data.DTOs.FavoriteRequest;
import com.example.app.data.DTOs.FavoriteResponse;
import com.example.app.data.entities.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface FavoritesControllerService {

    ResponseEntity<String> addToFavorites(@RequestBody FavoriteRequest request,
                                          HttpServletResponse response);

    ResponseEntity<String> removeFromFavorites(@RequestBody FavoriteRequest request,
                                               HttpServletResponse response);

    ResponseEntity<List<FavoriteResponse>> getFavVehicles(@RequestParam String id, String authToken,
                                                          HttpServletResponse response);
}
