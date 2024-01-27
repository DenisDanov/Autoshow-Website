package com.example.demo.dbData;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteVehiclesRepository extends JpaRepository<FavoriteVehiclesEntity, Long> {

    List<FavoriteVehiclesEntity> findByUser_Id(Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM FavoriteVehiclesEntity fv WHERE fv.vehicleId = :vehicleId AND fv.user.id = :userId")
    int deleteByVehicleIdAndUserId(String vehicleId, Long userId);

    FavoriteVehiclesEntity findByVehicleId(String vehicleId);

}
