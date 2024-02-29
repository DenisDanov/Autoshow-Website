package com.example.app.dbData.SecurityConfigs;

import com.example.app.dbData.FavoriteVehiclesRepository;
import com.example.app.dbData.User;
import com.example.app.dbData.UserRepository;
import com.example.app.dbData.orderCarService.CarOrdersEntity;
import com.example.app.dbData.orderCarService.CarOrdersRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class AccessController {

    private final FavoriteVehiclesRepository favoriteVehiclesRepository;

    private final CarOrdersRepository carOrdersRepository;

    private final UserRepository userRepository;

    @Autowired
    public AccessController(FavoriteVehiclesRepository favoriteVehiclesRepository,
                            UserRepository userRepository,
                            CarOrdersRepository carOrdersRepository) {
        this.favoriteVehiclesRepository = favoriteVehiclesRepository;
        this.userRepository = userRepository;
        this.carOrdersRepository = carOrdersRepository;
    }

    @GetMapping("/showroom.html")
    public ModelAndView showroomPage(@RequestParam String car, HttpServletRequest request) {
        return checkAccess("showroom", car, request);
    }

    @GetMapping("/cars-info.html")
    public ModelAndView carInfoPage(@RequestParam String car, HttpServletRequest request) {
        return checkAccess("cars-info", car, request);
    }

    private ModelAndView checkAccess(String pageUrl, String car, HttpServletRequest request) {
        String authToken = CookieUtils.getAuthTokenCookie(request);
        long userId = 0;
        car = "danov-autoshow.azurewebsites.net/showroom.html?car=" + car;
        String carValues = car.split("3D Models/")[1];
        car = car.replaceAll(" ", "%20");
        Optional<User> user = Optional.empty();

        if (authToken != null) {
            userId = CookieUtils.getUserIdFromAuthToken(authToken);
            user = userRepository.findById(userId);
        }

        if (user.isPresent() && (favoriteVehiclesRepository.findByVehicleIdAndUser(car, user.get()) != null ||
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
        for (CarOrdersEntity carOrdersEntity : carOrdersRepository.findByUser_Id(user.getId())) {
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
