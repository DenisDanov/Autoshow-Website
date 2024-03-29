package com.example.app.data.DTOs;

public class RecentlyViewedCarDTO {

    private String vehicleId;
    private String vehicleImg;
    private String vehicleName;
    private String carFav;
    private String carFavInput;

    public String getCarFav() {
        return carFav;
    }

    public void setCarFav(String carFav) {
        this.carFav = carFav;
    }

    public String getCarFavInput() {
        return carFavInput;
    }

    public void setCarFavInput(String carFavInput) {
        this.carFavInput = carFavInput;
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
