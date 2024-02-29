package com.example.app.dbData;

public class FavoriteResponse {

    private String vehicleId;
    private String vehicleImg;
    private String vehicleName;

    public FavoriteResponse() {
    }

    public FavoriteResponse(String vehicleId, String vehicleImg, String vehicleName) {
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
