package com.example.app.services.impl;

import com.example.app.data.entities.FavoriteVehiclesEntity;
import com.example.app.data.entities.User;
import com.example.app.data.repositories.FavoriteVehiclesRepository;
import com.example.app.services.FavoriteVehiclesService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteVehiclesServiceImpl implements FavoriteVehiclesService {

    private final FavoriteVehiclesRepository favoriteVehiclesRepository;

    public FavoriteVehiclesServiceImpl(FavoriteVehiclesRepository favoriteVehiclesRepository) {
        this.favoriteVehiclesRepository = favoriteVehiclesRepository;
    }

    @Override
    public List<FavoriteVehiclesEntity> findByUser_Id(Long userId) {
        return this.favoriteVehiclesRepository.findByUser_Id(userId);
    }

    @Override
    public int deleteByVehicleIdAndUserId(String vehicleId, Long userId) {
        return this.favoriteVehiclesRepository.deleteByVehicleIdAndUserId(vehicleId, userId);
    }

    @Override
    public FavoriteVehiclesEntity findByVehicleIdAndUser(String vehicleId, User user) {
        return this.favoriteVehiclesRepository.findByVehicleIdAndUser(vehicleId, user);
    }

    @Override
    public void save(FavoriteVehiclesEntity favoriteVehiclesEntity) {
        this.favoriteVehiclesRepository.save(favoriteVehiclesEntity);
    }
}
