package com.example.app.data.repositories;

import com.example.app.data.entities.CarOrderSpecData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarOrderSpecDataRepository extends JpaRepository<CarOrderSpecData,Long> {

    @Query("SELECT c FROM CarOrderSpecData c WHERE :car LIKE CONCAT('%', c.makeDisplay, '%') " +
            "AND :car LIKE CONCAT('%', c.modelName, '%') " +
            "AND :car LIKE CONCAT('%', c.modelYear, '%')")
    Optional<CarOrderSpecData> findCarOrderSpecDataByMakeDisplayAndModelNameAndModelYear(String car);

}
