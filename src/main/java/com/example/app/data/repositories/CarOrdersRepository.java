package com.example.app.data.repositories;

import com.example.app.data.entities.CarOrdersEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarOrdersRepository extends JpaRepository<CarOrdersEntity, Long> {

    List<CarOrdersEntity> findByUser_Id(Long userId);

    Optional<CarOrdersEntity> findByUser_IdAndCarManufacturerAndCarModelAndCarYear(
            Long userId, String carManufacturer, String carModel, int carYear
    );

    @Transactional
    @Modifying
    @Query("DELETE FROM CarOrdersEntity cr WHERE cr.carManufacturer = :carManufacturer AND cr.user.id = :userId AND cr.carModel = :carModel AND cr.carYear = :carYear")
    int deleteCarOrder(String carManufacturer, Long userId, String carModel, String carYear);

}
