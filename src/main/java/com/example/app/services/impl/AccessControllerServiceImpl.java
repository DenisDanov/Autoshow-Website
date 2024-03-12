package com.example.app.services.impl;

import com.example.app.controllers.utils.CookieUtils;
import com.example.app.data.entities.CarOrdersEntity;
import com.example.app.data.entities.FavoriteVehiclesEntity;
import com.example.app.data.entities.User;
import com.example.app.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.app.controllers.utils.CarOrderStatusCheckUtil.checkOrderStatus;

@Service
public class AccessControllerServiceImpl implements AccessControllerService {

    private final FavoriteVehiclesService favoriteVehiclesService;

    private final CarOrdersService carOrdersService;

    private final AuthenticationTokenService authenticationTokenService;

    private final ReplacedAuthTokensService replacedAuthTokensService;

    private final UserService userService;

    public AccessControllerServiceImpl(FavoriteVehiclesService favoriteVehiclesService,
                                       CarOrdersService carOrdersService,
                                       AuthenticationTokenService authenticationTokenService, ReplacedAuthTokensService replacedAuthTokensService, UserService userService) {
        this.favoriteVehiclesService = favoriteVehiclesService;
        this.carOrdersService = carOrdersService;
        this.authenticationTokenService = authenticationTokenService;
        this.replacedAuthTokensService = replacedAuthTokensService;
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
                isVehicleOrdered(user.get(), carValues))) {
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

    @Override
    public ModelAndView checkAccess(String pageUrl, HttpServletRequest request) throws URISyntaxException {
        String authToken = CookieUtils.getAuthTokenCookie(request);
        if (authToken != null && CookieUtils.getUserIdFromAuthToken(authToken) != 0L &&
                (authenticationTokenService.findByToken(authToken) != null &&
                        authenticationTokenService.findByToken(authToken).getUser().getId()
                                == CookieUtils.getUserIdFromAuthToken(authToken))) {
            ModelAndView pageWithContentLoaded = getModelAndViewProfilePage(authToken, pageUrl);
            if (pageWithContentLoaded != null) return pageWithContentLoaded;
        } else if (authToken != null && CookieUtils.getUserIdFromAuthToken(authToken) != 0L &&
                (replacedAuthTokensService.findByReplacedToken(authToken) != null &&
                        replacedAuthTokensService.findByReplacedToken(authToken).getUser().getId() ==
                                CookieUtils.getUserIdFromAuthToken(authToken))) {
            ModelAndView pageWithContentLoaded = getModelAndViewProfilePage(authToken, pageUrl);
            if (pageWithContentLoaded != null) return pageWithContentLoaded;
        }
        return new ModelAndView("index");
    }

    private ModelAndView getModelAndViewProfilePage(String authToken, String pageUrl) throws URISyntaxException {
        long userId = CookieUtils.getUserIdFromAuthToken(authToken);
        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            ModelAndView modelAndView = new ModelAndView(pageUrl)
                    .addObject("username", user.getUsername())
                    .addObject("email", user.getEmail());
            List<String> favVehicleContents = new ArrayList<>();
            List<String> carOrderContents = new ArrayList<>();
            for (FavoriteVehiclesEntity vehicle : favoriteVehiclesService.findByUser_Id(userId)) {
                String htmlContent = """
                        <div class="car-card">
                            <div class="img-container">
                                <img src="%s" alt="Car 2">
                            </div>
                            <div class="car-info">
                                <h3>%s</h3>
                            </div>
                            <div class="favorites">
                                <h3>Remove from Favorites</h3>
                                <label class="add-fav">
                                    <input checked="true" type="checkbox" />
                                    <i class="icon-heart fas fa-heart">
                                        <i class="icon-plus-sign fa-solid fa-plus"></i>
                                    </i>
                                </label>
                            </div>
                            <a href="showroom.html?car=%s" class="view-button">View in Showroom</a>
                        </div>
                        """.formatted(vehicle.getVehicleImg(), vehicle.getVehicleName(), vehicle.getVehicleId());
                favVehicleContents.add(htmlContent);
            }
            for (CarOrdersEntity carOrdersEntity : carOrdersService.findByUser_Id(userId)) {
                if (carOrdersEntity.getOrderStatus().equals("Pending")) {
                    checkOrderStatus(carOrdersService,carOrdersEntity);
                }
                String hideChangeOrderBtn;
                String hideCarCardContent;
                String hideChangeBtnAndShowExpired;
                String modifyReference = "";
                if (carOrdersEntity.getOrderStatus().equals("Completed")) {
                    hideChangeOrderBtn = "none";
                    hideCarCardContent = "flex";
                    hideChangeBtnAndShowExpired = "none";
                } else if (carOrdersEntity.getOrderStatus().equals("Expired")){
                    hideCarCardContent = "none";
                    hideChangeOrderBtn = "none";
                    hideChangeBtnAndShowExpired = "";
                    modifyReference = "false";
                } else {
                    hideCarCardContent = "none";
                    hideChangeOrderBtn = "";
                    hideChangeBtnAndShowExpired = "none";
                }
                String[] favsResult = isOrderAddedToFavs(favoriteVehiclesService.findByUser_Id(userId), carOrdersEntity);
                String htmlContent = """
                                    <div class="car-orders-container">
                            <div class="car-order-details">
                                <div>
                                    <span>Car manufacturer</span>
                                    <p class="car-manufacturer">%s</p>
                                </div>
                                <div>
                                    <span>Car model</span>
                                    <p class="car-model">%s</p>
                                </div>
                                <div>
                                    <span>Manufacture year</span>
                                    <p class="car-year">%d</p>
                                </div>
                            </div>
                            <div class="car-order-status">
                                <div>
                                    <span>Order status</span>
                                    <p class="order-status" status="%s">%s</p>
                                </div>
                                <div>
                                    <span>Order date</span>
                                    <p>%s</p>
                                </div>
                            </div>
                            <div class="car-order-model" style="display: %s">
                                <h1>Ordered car</h1>
                                <div class="car-card">
                                    <div class="img-container">
                                        <img src="images/%s-%s-%d.png" alt="Car 2">
                                    </div>
                                    <div class="car-info">
                                        <h3>%d %s %s</h3>
                                    </div>
                                    <div class="favorites">
                                        <h3>%s</h3>
                                        <label class="add-fav">
                                            <input type="checkbox" %s />
                                            <i class="icon-heart fas fa-heart">
                                                <i class="icon-plus-sign fa-solid fa-plus"></i>
                                            </i>
                                        </label>
                                    </div>
                                    <a href="showroom.html?car=3D Models/%s-%s-%d.glb"
                                        class="view-button">View in
                                        Showroom</a>
                                </div>
                            </div>
                            <div id="cancel-order-container">
                            <button id="change-order" style="display: %s">Change Order</button>
                            <button id="modify-order" style="display: %s" modify-reference="%s">Modify Order</button>
                            <button id="cancel-order">Cancel Order</button>
                        </div>
                        </div>
                                    """.formatted(carOrdersEntity.getCarManufacturer(), carOrdersEntity.getCarModel(),
                        carOrdersEntity.getCarYear(), carOrdersEntity.getOrderStatus(), carOrdersEntity.getOrderStatus(),
                        carOrdersEntity.getDateOfOrder(), hideCarCardContent, carOrdersEntity.getCarManufacturer(),
                        carOrdersEntity.getCarModel(), carOrdersEntity.getCarYear(), carOrdersEntity.getCarYear(),
                        carOrdersEntity.getCarManufacturer(), carOrdersEntity.getCarModel(), favsResult[0], favsResult[1],
                        carOrdersEntity.getCarManufacturer(), carOrdersEntity.getCarModel(), carOrdersEntity.getCarYear(),
                        hideChangeOrderBtn,hideChangeBtnAndShowExpired, modifyReference);
                carOrderContents.add(htmlContent);
            }
            modelAndView.addObject("carOrderContents", carOrderContents);
            modelAndView.addObject("favVehicleContents", favVehicleContents);
            return modelAndView;
        }
        return new ModelAndView("index");
    }

    private String[] isOrderAddedToFavs(List<FavoriteVehiclesEntity> favoriteVehiclesEntities, CarOrdersEntity
            carOrdersEntity) {
        for (FavoriteVehiclesEntity favoriteVehiclesEntity : favoriteVehiclesEntities) {
            if (favoriteVehiclesEntity.getVehicleId().
                    contains("3D Models/%s-%s-%d".formatted(carOrdersEntity.getCarManufacturer(),
                            carOrdersEntity.getCarModel(), carOrdersEntity.getCarYear()))) {
                return new String[]{"Remove from Favorites", "checked=" + "true"};
            }
        }
        return new String[]{"Add to Favorites", ""};
    }

    private boolean isVehicleOrdered(User user, String carValues) {
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
