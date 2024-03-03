package com.example.app.data.DTOs;

public class RemoveCarOrderRequest {
    private String id;
    private String carManufacturer;
    private String carModel;
    private String carYear;
    private String authToken;

    public String getId() {
        return id;
    }

    public String getCarManufacturer() {
        return carManufacturer;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getCarYear() {
        return carYear;
    }

    public String getAuthToken() {
        return authToken;
    }
}
