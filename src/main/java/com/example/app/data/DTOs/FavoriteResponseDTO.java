package com.example.app.data.DTOs;

public class FavoriteResponseDTO {

    private String vehicleId;
    private String vehicleImg;
    private String vehicleName;

    public FavoriteResponseDTO() {
    }

    public FavoriteResponseDTO(String vehicleId, String vehicleImg, String vehicleName) {
        this.vehicleId = vehicleId;
        this.vehicleImg = vehicleImg;
        this.vehicleName = vehicleName;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleImg() {
        return vehicleImg;
    }

    public void setVehicleImg(String vehicleImg) {
        this.vehicleImg = vehicleImg;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }
}
