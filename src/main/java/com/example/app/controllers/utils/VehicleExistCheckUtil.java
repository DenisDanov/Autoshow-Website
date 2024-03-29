package com.example.app.controllers.utils;

import com.example.app.data.entities.CarOrdersEntity;
import com.example.app.data.entities.FavoriteVehiclesEntity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class VehicleExistCheckUtil {

    public static boolean doesItExist(String filePath) {
        try {
            // Load the resource as a classpath resource
            Resource resource = new ClassPathResource("public/images/" + filePath + ".png");
            // Try to open an InputStream to check if the resource exists
            InputStream inputStream = resource.getInputStream();
            // If InputStream is not null the resource exists
            return inputStream != null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
