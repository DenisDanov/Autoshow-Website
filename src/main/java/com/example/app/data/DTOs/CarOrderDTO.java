package com.example.app.data.DTOs;

import java.sql.Date;
import java.time.LocalDate;

public class CarOrderDTO {

    private String carManufacturer;
    private String carModel;
    private String carYear;
    private String orderStatus;
    private Date dateOfOrder;

    public CarOrderDTO() {
    }

    public CarOrderDTO(String carManufacturer, String carModel, String carYear, String orderStatus, Date dateOfOrder) {
        this.carManufacturer = carManufacturer;
        this.carModel = carModel;
        this.carYear = carYear;
        this.orderStatus = orderStatus;
        this.dateOfOrder = dateOfOrder;
    }

    public void setDateOfOrder(String dateOfOrder) {
        this.dateOfOrder = Date.valueOf(dateOfOrder);
    }

    public Date getDateOfOrder() {
        return dateOfOrder;
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

    public String getCarYear() {
        return carYear;
    }

    public void setCarYear(String carYear) {
        this.carYear = carYear;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
