package com.example.app.data.entities;

import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "car_orders")
public class CarOrdersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;
    @Column(name = "car_manufacturer")
    private String carManufacturer;
    @Column(name = "car_model")
    private String carModel;
    @Column(name = "car_year")
    private int carYear;
    @Column(name = "order_status")
    private String orderStatus;
    @Column(name = "date_of_order")
    private Date dateOfOrder;

    public CarOrdersEntity() {
    }

    public CarOrdersEntity(User user,
                           String carManufacturer,
                           String carModel,
                           int carYear) {
        this.user = user;
        this.carManufacturer = carManufacturer;
        this.carModel = carModel;
        this.carYear = carYear;
        this.orderStatus = "Pending";
        this.dateOfOrder = new Date(System.currentTimeMillis());
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

    public void setDateOfOrder(Date dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }
}
