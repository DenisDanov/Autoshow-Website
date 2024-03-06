package com.example.app.services.impl;

import com.example.app.controllers.utils.CookieUtils;
import com.example.app.data.entities.CarOrdersEntity;
import com.example.app.data.entities.User;
import com.example.app.services.AccessControllerService;
import com.example.app.services.CarOrdersService;
import com.example.app.services.FavoriteVehiclesService;
import com.example.app.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Service
public class AccessControllerServiceImpl implements AccessControllerService {

    private final FavoriteVehiclesService favoriteVehiclesService;

    private final CarOrdersService carOrdersService;

    private final UserService userService;

    public AccessControllerServiceImpl(FavoriteVehiclesService favoriteVehiclesService,
                                       CarOrdersService carOrdersService,
                                       UserService userService) {
        this.favoriteVehiclesService = favoriteVehiclesService;
        this.carOrdersService = carOrdersService;
        this.userService = userService;
    }

    @Override
    public ModelAndView checkAccess(String pageUrl, String car, HttpServletRequest request) {
        String authToken = CookieUtils.getAuthTokenCookie(request);
        long userId = 0;
        String carValues = car.split("3D Models/")[1];
        Optional<User> user = Optional.empty();

        if (authToken != null) {
            userId = CookieUtils.getUserIdFromAuthToken(authToken);
            user = userService.findById(userId);
        }

        if (user.isPresent() && (favoriteVehiclesService.findByVehicleIdAndUser(car, user.get()) != null ||
                isVehicleOrdered(user.get(),carValues))) {
            // User is authorized to view this vehicle
            return new ModelAndView(pageUrl)
                    .addObject("access", "granted-access");
        } else if (isVehicleWhitelisted(carValues)) {
            return new ModelAndView(pageUrl)
                    .addObject("access", "granted-access");
        } else {
            // User is not authorized to view this vehicle
            return new ModelAndView(pageUrl)
                    .addObject("access", "no-access");
        }
    }

    private boolean isVehicleOrdered(User user,String carValues) {
        for (CarOrdersEntity carOrdersEntity : carOrdersService.findByUser_Id(user.getId())) {
            if (carValues.contains(carOrdersEntity.getCarManufacturer()) &&
                    carValues.contains(carOrdersEntity.getCarModel()) &&
                    carValues.contains(String.valueOf(carOrdersEntity.getCarYear()))) {
                return true;
            }
        }
        return false;
    }

    private boolean isVehicleWhitelisted(String carValues) {
        return switch (carValues) {
            case "Lamborghini-Urus-2020.glb",
                    "Tesla-Model-3-2020.glb",
                    "Mclaren-P1-2015.glb",
                    "BMW-X5-2022.glb",
                    "Porsche-Boxster-2016.glb",
                    "Toyota-Gr-Supra-2020.glb",
                    "Lamborghini-Gallardo-2007.glb",
                    "Lamborghini-Aventador-2020.glb",
                    "Porsche-Carrera-2015.glb" -> true;
            default -> false;
        };
    }
}
