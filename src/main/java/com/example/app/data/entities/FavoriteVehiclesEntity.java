package com.example.app.data.entities;

import com.example.app.data.entities.User;
import jakarta.persistence.*;

@Entity
@Table(name = "favorite_vehicles")
public class FavoriteVehiclesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "vehicle_id")
    private String vehicleId;

    private String vehicleImg;

    private String vehicleName;

    public FavoriteVehiclesEntity() {
    }

    public FavoriteVehiclesEntity(String vehicleId, String vehicleImg, String vehicleName, User user) {
        this.vehicleId = vehicleId;
        this.vehicleImg = vehicleImg;
        this.vehicleName = vehicleName;
        this.user = user;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }
}
