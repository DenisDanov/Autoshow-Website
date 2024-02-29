package com.example.app.dbData;

import java.util.List;

public class ProfileResponse {
    private String username;

    private String email;

    private List<FavoriteResponse> favVehicles;

    public ProfileResponse() {
    }

    public ProfileResponse(String username, String email, List<FavoriteResponse> favVehicles) {
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

    public List<FavoriteResponse> getFavVehicles() {
        return favVehicles;
    }

    public void setFavVehicles(List<FavoriteResponse> favVehicles) {
        this.favVehicles = favVehicles;
    }
}
