package com.example.app.services.impl;

import com.example.app.data.entities.CarOrdersEntity;
import com.example.app.data.repositories.CarOrdersRepository;
import com.example.app.services.CarOrdersService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarOrdersServiceImpl implements CarOrdersService {

    private final CarOrdersRepository carOrdersRepository;

    public CarOrdersServiceImpl(CarOrdersRepository carOrdersRepository) {
        this.carOrdersRepository = carOrdersRepository;
    }

    @Override
    public List<CarOrdersEntity> findByUser_Id(Long userId) {
        return this.carOrdersRepository.findByUser_Id(userId);
    }

    @Override
    public Optional<CarOrdersEntity> findByUser_IdAndCarManufacturerAndCarModelAndCarYear(Long userId, String carManufacturer, String carModel, int carYear) {
        return this.carOrdersRepository.findByUser_IdAndCarManufacturerAndCarModelAndCarYear(userId,
                carManufacturer,
                carModel,
                carYear);
    }

    @Override
    public int deleteCarOrder(String carManufacturer, Long userId, String carModel, String carYear) {
        return this.carOrdersRepository.deleteCarOrder(carManufacturer,userId,carModel,carYear);
    }

    @Override
    public CarOrdersEntity save(CarOrdersEntity carOrdersEntity) {
        return this.carOrdersRepository.save(carOrdersEntity);
    }
}
