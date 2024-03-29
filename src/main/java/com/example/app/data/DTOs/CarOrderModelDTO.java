package com.example.app.data.DTOs;

import java.sql.Date;

public class CarOrderModelDTO {

    private String carManufacturer;
    private String carModel;
    private int carYear;
    private String orderStatus;
    private Date dateOfOrder;
    private String carImg;
    private String carFullName;
    private String carFav;
    private String carFavInput;

    public String getCarImg() {
        return carImg;
    }

    public void setCarImg(String carImg) {
        this.carImg = carImg;
    }

    public String getCarFullName() {
        return carFullName;
    }

    public void setCarFullName(String carFullName) {
        this.carFullName = carFullName;
    }

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

    public String getCarManufacturer() {
        return carManufacturer;
    }

    public void setCarManufacturer(String carManufacturer) {
        this.carManufacturer = carManufacturer;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public int getCarYear() {
        return carYear;
    }

    public void setCarYear(int carYear) {
        this.carYear = carYear;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(Date dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }
}
