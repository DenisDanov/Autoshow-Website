package com.example.app.services;

import com.example.app.data.entities.CarOrderSpecData;

import java.util.Optional;

public interface CarOrderSpecDataService {

    Optional<CarOrderSpecData> findCarOrderSpecDataByMakeDisplayAndModelNameAndModelYear(String text);

    void seedDatabase();
}
