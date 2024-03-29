package com.example.app.data.DTOs;

import java.util.List;

public class ProfileResponseDTO {
    private String username;

    private String email;

    private List<FavoriteResponseDTO> favVehicles;

    public ProfileResponseDTO() {
    }

    public ProfileResponseDTO(String username, String email, List<FavoriteResponseDTO> favVehicles) {
        this.username = username;
        this.email = email;
        this.favVehicles = favVehicles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<FavoriteResponseDTO> getFavVehicles() {
        return favVehicles;
    }

    public void setFavVehicles(List<FavoriteResponseDTO> favVehicles) {
        this.favVehicles = favVehicles;
    }
}
