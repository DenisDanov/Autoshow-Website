package com.example.app.data.DTOs;

public class CarDTO {

    private String carImg;
    private String carName;
    private String carModelPath;
    private boolean isInFavorites;

    public CarDTO(String carImg, String carName, String carModelPath, boolean isInFavorites) {
        this.carImg = carImg;
        this.carName = carName;
        this.carModelPath = carModelPath;
        this.isInFavorites = isInFavorites;
    }

    public String getCarModelPath() {
        return carModelPath;
    }

    public void setCarModelPath(String carModelPath) {
        this.carModelPath = carModelPath;
    }

    public String getCarImg() {
        return carImg;
    }

    public void setCarImg(String carImg) {
        this.carImg = carImg;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public boolean isInFavorites() {
        return isInFavorites;
    }

    public void setInFavorites(boolean inFavorites) {
        isInFavorites = inFavorites;
    }
}
