package com.example.app.services;

import com.example.app.data.entities.CarOrdersEntity;

import java.util.List;
import java.util.Optional;

public interface CarOrdersService {

    List<CarOrdersEntity> findByUser_Id(Long userId);

    Optional<CarOrdersEntity> findByUser_IdAndCarManufacturerAndCarModelAndCarYear(
            Long userId, String carManufacturer, String carModel, int carYear
    );

    int deleteCarOrder(String carManufacturer, Long userId, String carModel, String carYear);

    CarOrdersEntity save(CarOrdersEntity carOrdersEntity);
}
