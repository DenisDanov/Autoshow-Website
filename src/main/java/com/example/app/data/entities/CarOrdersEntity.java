package com.example.app.data.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "car_orders")
public class CarOrdersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private String carManufacturer;
    private String carModel;
    private int carYear;
    private String orderStatus;
    private java.sql.Date dateOfOrder;

    public CarOrdersEntity() {
    }

    public CarOrdersEntity(User user,
                           String carManufacturer,
                           String carModel,
                           int carYear,
                           String orderStatus,
                           java.sql.Date dateOfOrder) {
        this.user = user;
        this.carManufacturer = carManufacturer;
        this.carModel = carModel;
        this.carYear = carYear;
        this.orderStatus = orderStatus;
        this.dateOfOrder = dateOfOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public void setDateOfOrder(java.sql.Date dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }
}
