package com.example.app.controllers.utils;

import com.example.app.data.entities.CarOrdersEntity;
import com.example.app.services.CarOrdersService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CarOrderStatusCheckUtil {

    public static void checkOrderStatus(CarOrdersService carOrdersService,CarOrdersEntity carOrdersEntity){
        LocalDate givenDate = LocalDate.parse(carOrdersEntity.getDateOfOrder().toString());

        // Current date
        LocalDate currentDate = LocalDate.now();

        // Calculate the difference in days
        long daysDifference = Math.abs(ChronoUnit.DAYS.between(givenDate, currentDate));
        if (daysDifference >= 7) {
            carOrdersEntity.setOrderStatus("Expired");
            carOrdersService.save(carOrdersEntity);
            return;
        }
        String directoryPattern = "classpath:public/3D Models/*";

        // Create a resource resolver
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        try {
            // Resolve resources matching the pattern
            Resource[] resources = resolver.getResources(directoryPattern);

            // Iterate over the resolved resources
            for (Resource resource : resources) {
                // Get the file name
                String fileName = resource.getFilename();
                if (fileName.contains(carOrdersEntity.getCarManufacturer()) &&
                        fileName.contains(carOrdersEntity.getCarModel()) &&
                        fileName.contains(String.valueOf(carOrdersEntity.getCarYear()))) {
                    carOrdersEntity.setOrderStatus("Completed");
                    carOrdersService.save(carOrdersEntity);
                    return;
                }
            }
            if (!carOrdersEntity.getOrderStatus().equals("Pending")) {
                carOrdersEntity.setOrderStatus("Pending");
                carOrdersService.save(carOrdersEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
