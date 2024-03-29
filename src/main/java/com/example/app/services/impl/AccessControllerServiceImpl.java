package com.example.app.services.impl;

import com.example.app.controllers.ViewController;
import com.example.app.controllers.utils.AuthTokenValidationUtil;
import com.example.app.controllers.utils.CookieUtils;
import com.example.app.data.DTOs.CarOrderModelDTO;
import com.example.app.data.DTOs.FavoriteResponseDTO;
import com.example.app.data.entities.CarOrdersEntity;
import com.example.app.data.entities.FavoriteVehiclesEntity;
import com.example.app.data.entities.User;
import com.example.app.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.app.controllers.utils.CarOrderStatusCheckUtil.checkOrderStatus;

@Service
public class AccessControllerServiceImpl implements AccessControllerService {

    private final FavoriteVehiclesService favoriteVehiclesService;

    private final RecentlyViewedTokenService recentlyViewedTokenService;

    private final CarOrdersService carOrdersService;

    private final UserService userService;

    private final AuthTokenValidationUtil authTokenValidationUtil;

    private final ModelMapper modelMapper;

    public AccessControllerServiceImpl(FavoriteVehiclesService favoriteVehiclesService,
                                       RecentlyViewedTokenService recentlyViewedTokenService, CarOrdersService carOrdersService,
                                       UserService userService, AuthTokenValidationUtil authTokenValidationUtil, ModelMapper modelMapper) {
        this.favoriteVehiclesService = favoriteVehiclesService;
        this.recentlyViewedTokenService = recentlyViewedTokenService;
        this.carOrdersService = carOrdersService;
        this.userService = userService;
        this.authTokenValidationUtil = authTokenValidationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public ModelAndView checkAccess(String pageUrl, String car, HttpServletRequest request, HttpServletResponse response) {
        String authToken = CookieUtils.getAuthTokenCookie(request);
        long userId;
        String carValues = car.split("3D Models/")[1];
        Optional<User> user = Optional.empty();
        String[] navHtmlAndToken = ViewController.getNavigationHtml(authToken, authTokenValidationUtil, response);
        String navigationHtml = navHtmlAndToken[0];
        authToken = navHtmlAndToken[1];

        if (authToken != null && !authToken.isEmpty()) {
            userId = CookieUtils.getUserIdFromAuthToken(authToken);
            user = userService.findById(userId);
        }

        if (user.isPresent() && (favoriteVehiclesService.findByVehicleIdAndUser(car, user.get()) != null ||
                isVehicleOrdered(user.get(), carValues))) {
            return new ModelAndView(pageUrl)
                    .addObject("nav", navigationHtml)
                    .addObject("access", "granted-access");
        } else if (isVehicleWhitelisted(carValues)) {
            return new ModelAndView(pageUrl)
                    .addObject("nav", navigationHtml)
                    .addObject("access", "granted-access");
        } else {
            return new ModelAndView(pageUrl)
                    .addObject("nav", navigationHtml)
                    .addObject("access", "no-access");
        }
    }

    @Override
    public ModelAndView checkAccess(String pageUrl, HttpServletRequest request, HttpServletResponse response) throws URISyntaxException {
        String authToken = CookieUtils.getAuthTokenCookie(request);
        if (authToken != null) {
            return getModelAndViewProfilePage(authToken, pageUrl, response, request);
        }
        return new ModelAndView("redirect:/index");
    }

    private ModelAndView getModelAndViewProfilePage(String authToken,
                                                    String pageUrl,
                                                    HttpServletResponse response,
                                                    HttpServletRequest request) throws URISyntaxException {
        String[] navHtmlAndToken = ViewController.getNavigationHtml(authToken, authTokenValidationUtil, response);
        String navigationHtml = navHtmlAndToken[0];
        authToken = navHtmlAndToken[1];
        long userId = CookieUtils.getUserIdFromAuthToken(authToken);
        Optional<User> userOptional = userService.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            ModelAndView modelAndView = new ModelAndView(pageUrl)
                    .addObject("nav", navigationHtml)
                    .addObject("username", user.getUsername())
                    .addObject("email", user.getEmail());
            if (Objects.equals(authToken, "")) {
                return new ModelAndView("redirect:/index");
            }
            List<FavoriteVehiclesEntity> favoriteVehiclesEntities = favoriteVehiclesService.findByUser_Id(userId);
            List<FavoriteResponseDTO> favVehicleContents = favoriteVehiclesEntities.stream().
                    map(favoriteVehiclesEntity -> modelMapper.map(favoriteVehiclesEntity, FavoriteResponseDTO.class))
                    .collect(Collectors.toList());
            List<CarOrderModelDTO> carOrderContents = generateCarOrderContent(carOrdersService.findByUser_Id(userId), favoriteVehiclesEntities);
            modelAndView.addObject("favVehicleContents", favVehicleContents);
            modelAndView.addObject("carOrderModels", carOrderContents);
            modelAndView.addObject("recentlyViewed", ViewController.getRecentlyViewedHtml(authToken
                    , recentlyViewedTokenService, authTokenValidationUtil, favoriteVehiclesEntities));
            return modelAndView;
        }
        return new ModelAndView("redirect:/index");
    }

    private List<CarOrderModelDTO> generateCarOrderContent(List<CarOrdersEntity> carOrders,
                                                           List<FavoriteVehiclesEntity> favoriteVehiclesEntities) {
        List<CarOrderModelDTO> carOrderContents = new ArrayList<>();
        for (CarOrdersEntity carOrdersEntity : carOrders) {
            if (carOrdersEntity.getOrderStatus().equals("Pending")) {
                checkOrderStatus(carOrdersService, carOrdersEntity);
            }
            CarOrderModelDTO carOrderModelDTO = modelMapper.map(carOrdersEntity, CarOrderModelDTO.class);
            String[] favsParts = isOrderAddedToFavs(favoriteVehiclesEntities, carOrdersEntity);
            carOrderModelDTO.setCarFullName(String.format("%d %s %s", carOrdersEntity.getCarYear(),
                    carOrdersEntity.getCarManufacturer(), carOrdersEntity.getCarModel()));
            carOrderModelDTO.setCarImg(String.format("%s-%s-%d", carOrdersEntity.getCarManufacturer(),
                    carOrdersEntity.getCarModel(), carOrdersEntity.getCarYear()));
            carOrderModelDTO.setCarFav(favsParts[0]);
            carOrderModelDTO.setCarFavInput(favsParts[1]);
            carOrderContents.add(carOrderModelDTO);
        }
        return carOrderContents;
    }

    private String[] isOrderAddedToFavs(List<FavoriteVehiclesEntity> favoriteVehiclesEntities, CarOrdersEntity
            carOrdersEntity) {
        for (FavoriteVehiclesEntity favoriteVehiclesEntity : favoriteVehiclesEntities) {
            if (favoriteVehiclesEntity.getVehicleId().
                    contains("3D Models/%s-%s-%d".formatted(carOrdersEntity.getCarManufacturer(),
                            carOrdersEntity.getCarModel(), carOrdersEntity.getCarYear()))) {
                return new String[]{"Remove from Favorites", "true"};
            }
        }
        return new String[]{"Add to Favorites", "false"};
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
            case "Lamborghini-Urus-2022.glb",
                    "Tesla-Model-3-2020.glb",
                    "Mclaren-P1-2015.glb",
                    "BMW-X5-2022.glb",
                    "Porsche-Boxster-2016.glb",
                    "Toyota-Supra-Gr-2020.glb",
                    "Lamborghini-Gallardo-2007.glb",
                    "Lamborghini-Aventador-2020.glb",
                    "Porsche-Carrera-2015.glb" -> true;
            default -> false;
        };
    }
}
