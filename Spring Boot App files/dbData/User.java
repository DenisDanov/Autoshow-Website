package com.example.demo.dbData;

import com.example.demo.dbData.orderCarService.CarOrder;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Convert;

@Entity
@Table
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    @Column(unique = true)
    private String username;
    private String email;
    private String password;

    @JsonProperty("favorite_vehicles")
    @Convert(converter = ListConverterFavVehicles.class)
    @Column(columnDefinition = "TEXT")
    private List<FavoriteResponse> favoriteVehicles;

    @JsonProperty("car_orders")
    @Convert(converter = CarOrderListConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<CarOrder> carOrders;

    public User() {
    }

    public User(Long id, String username, String userPassword, String email) {
        this.id = id;
        this.username = username;
        this.password = userPassword;
        this.email = email;
        this.favoriteVehicles = new ArrayList<>();
        this.carOrders = new ArrayList<>();
    }

    public User(String username, String userPassword,String email) {
        this.username = username;
        this.password = userPassword;
        this.email = email;
        this.favoriteVehicles = new ArrayList<>();
        this.carOrders = new ArrayList<>();
    }

    public List<CarOrder> getCarOrders() {
        return carOrders;
    }

    public void setCarOrders(List<CarOrder> carOrders) {
        this.carOrders = carOrders;
    }

    public List<FavoriteResponse> getFavoriteVehicles() {
        return favoriteVehicles;
    }

    public void setFavoriteVehicles(List<FavoriteResponse> favoriteVehicles) {
        this.favoriteVehicles = favoriteVehicles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String userPassword) {
        this.password = userPassword;
    }
}
