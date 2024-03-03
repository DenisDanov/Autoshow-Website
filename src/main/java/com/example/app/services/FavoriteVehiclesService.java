package com.example.app.services;

import com.example.app.data.entities.FavoriteVehiclesEntity;
import com.example.app.data.entities.User;

import java.util.List;

public interface FavoriteVehiclesService {

    List<FavoriteVehiclesEntity> findByUser_Id(Long userId);

    int deleteByVehicleIdAndUserId(String vehicleId, Long userId);

    FavoriteVehiclesEntity findByVehicleIdAndUser(String vehicleId, User user);

    void save(FavoriteVehiclesEntity favoriteVehiclesEntity);
}
