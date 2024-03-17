package com.example.app.data.repositories;

import com.example.app.data.entities.CarOrderSpecData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarOrderSpecDataRepository extends JpaRepository<CarOrderSpecData,Long> {

    Optional<CarOrderSpecData> findCarOrderSpecDataByMakeDisplayAndModelNameAndModelYear(String make,String model,int year);

}
