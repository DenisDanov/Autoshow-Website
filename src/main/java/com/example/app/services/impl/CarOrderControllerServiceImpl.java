package com.example.app.services.impl;

import com.example.app.data.DTOs.*;
import com.example.app.data.entities.AuthenticationToken;
import com.example.app.data.entities.CarOrdersEntity;
import com.example.app.data.entities.User;
import com.example.app.services.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.app.controllers.utils.CarOrderStatusCheckUtil.checkOrderStatus;

@Service
public class CarOrderControllerServiceImpl implements CarOrderControllerService {

    private final UserService userService;

    private final CarOrdersService carOrdersService;

    private final AuthenticationTokenService authenticationTokenService;

    private final ReplacedAuthTokensService replacedAuthTokensService;

    public CarOrderControllerServiceImpl(UserService userService,
                                         CarOrdersServiceImpl carOrdersService,
                                         AuthenticationTokenService authenticationTokenService,
                                         ReplacedAuthTokensService replacedAuthTokensService) {
        this.userService = userService;
        this.carOrdersService = carOrdersService;
        this.authenticationTokenService = authenticationTokenService;
        this.replacedAuthTokensService = replacedAuthTokensService;
    }

    @Override
    public ResponseEntity<String> addOrder(CarOrderRequest request, HttpServletResponse response) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getId());
        Optional<User> userOptional = userService.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokenService.findByToken(request.getAuthToken());
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            User user = userOptional.get();

            CarOrder carOrder = new CarOrder(request.getCarManufacturer(),
                    request.getCarModel(),
                    request.getCarYear());

            List<CarOrdersEntity> carOrders = carOrdersService.findByUser_Id(userId);
            if (!carOrders.isEmpty()) {
                if (carOrders.stream().noneMatch(carOrder1 ->
                        carOrder1.getCarManufacturer().equals(carOrder.getCarManufacturer()) &&
                                carOrder1.getCarModel().equals(carOrder.getCarModel()) &&
                                carOrder1.getCarYear() == (Integer.parseInt(carOrder.getCarYear())))) {
                    carOrdersService.save(new CarOrdersEntity(user, carOrder.getCarManufacturer(),
                            carOrder.getCarModel(), Integer.parseInt(carOrder.getCarYear()), carOrder.getOrderStatus(),
                            carOrder.getDateOfOrder()));
                    userService.save(user);
                    return ResponseEntity.ok("Order sent successfully.");
                } else {
                    return ResponseEntity.ok("Car order already exists.");
                }
            } else {
                carOrdersService.save(new CarOrdersEntity(user, carOrder.getCarManufacturer(),
                        carOrder.getCarModel(), Integer.parseInt(carOrder.getCarYear()), carOrder.getOrderStatus(),
                        carOrder.getDateOfOrder()));
                return ResponseEntity.ok("Order sent successfully.");
            }
        } else {
            authenticationToken = authenticationTokenService.findByUser_Id(userId);
            if (userOptional.isPresent() && authenticationToken != null &&
                    replacedAuthTokensService.findByReplacedToken(request.getAuthToken()) != null) {

                Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                cookie.setMaxAge((int) maxAgeInSeconds);
                cookie.setPath("/"); // Save the cookie for all pages of the site

                response.addCookie(cookie);
                User user = userOptional.get();

                CarOrder carOrder = new CarOrder(request.getCarManufacturer(),
                        request.getCarModel(),
                        request.getCarYear());

                List<CarOrdersEntity> carOrders = carOrdersService.findByUser_Id(userId);
                if (!carOrders.isEmpty()) {
                    if (carOrders.stream().noneMatch(carOrder1 ->
                            carOrder1.getCarManufacturer().equals(carOrder.getCarManufacturer()) &&
                                    carOrder1.getCarModel().equals(carOrder.getCarModel()) &&
                                    carOrder1.getCarYear() == (Integer.parseInt(carOrder.getCarYear())))) {
                        carOrdersService.save(new CarOrdersEntity(user, carOrder.getCarManufacturer(),
                                carOrder.getCarModel(), Integer.parseInt(carOrder.getCarYear()), carOrder.getOrderStatus(),
                                carOrder.getDateOfOrder()));
                        userService.save(user);
                        return ResponseEntity.ok("Order sent successfully.");
                    } else {
                        return ResponseEntity.ok("Car order already exists.");
                    }
                } else {
                    carOrdersService.save(new CarOrdersEntity(user, carOrder.getCarManufacturer(),
                            carOrder.getCarModel(), Integer.parseInt(carOrder.getCarYear()), carOrder.getOrderStatus(),
                            carOrder.getDateOfOrder()));
                    return ResponseEntity.ok("Order sent successfully.");
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @Override
    public ResponseEntity<List<CarOrder>> getOrders(Long userId, HttpServletResponse response) {
        // Find the user by ID
        Optional<User> userOptional = userService.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokenService.findByUser_Id(userId);
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            List<CarOrder> getAllOrders = carOrdersService
                    .findByUser_Id(userId)
                    .stream()
                    .map(carOrder -> new CarOrder(
                            carOrder.getCarManufacturer(),
                            carOrder.getCarModel(),
                            String.valueOf(carOrder.getCarYear()),
                            carOrder.getOrderStatus(),
                            (Date) carOrder.getDateOfOrder()))
                    .toList();
            return ResponseEntity.ok(getAllOrders);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Override
    public ResponseEntity<String> removeCarOrder(RemoveCarOrderRequest request, HttpServletResponse response) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getId());
        Optional<User> userOptional = userService.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokenService.findByToken(request.getAuthToken());
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            if (carOrdersService.deleteCarOrder(request.getCarManufacturer(),
                    userId,
                    request.getCarModel(),
                    request.getCarYear()) > 0) {
                return ResponseEntity.ok("Car order successfully removed.");
            } else {
                return ResponseEntity.ok("Car order doesn't exist.");
            }
        } else {
            authenticationToken = authenticationTokenService.findByUser_Id(userId);
            if (userOptional.isPresent() && authenticationToken != null &&
                    replacedAuthTokensService.findByReplacedToken(request.getAuthToken()) != null) {

                Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                cookie.setMaxAge((int) maxAgeInSeconds);
                cookie.setPath("/"); // Save the cookie for all pages of the site

                response.addCookie(cookie);
                if (carOrdersService.deleteCarOrder(request.getCarManufacturer(),
                        userId,
                        request.getCarModel(),
                        request.getCarYear()) > 0) {
                    return ResponseEntity.ok("Car order successfully removed.");
                } else {
                    return ResponseEntity.ok("Car order doesn't exist.");
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @Override
    public ResponseEntity<ModifyCarOrderResponse> modifyCarOrder(ModifyCarOrder request, HttpServletResponse response) {
        Long userId = Long.parseLong(request.getId());
        Optional<User> userOptional = userService.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokenService.findByToken(request.getAuthToken());
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            if (carOrdersService.findByUser_IdAndCarManufacturerAndCarModelAndCarYear(userId,
                    request.getCurrentManufacturer(),
                    request.getCurrentModel(),
                    Integer.parseInt(request.getCurrentYear())).isPresent()) {

                CarOrdersEntity carOrder = carOrdersService.findByUser_IdAndCarManufacturerAndCarModelAndCarYear(userId,
                        request.getCurrentManufacturer(),
                        request.getCurrentModel(),
                        Integer.parseInt(request.getCurrentYear())).get();

                if (carOrder.getCarManufacturer().equals(request.getNewManufacturer()) &&
                        carOrder.getCarModel().equals(request.getNewModel()) &&
                        carOrder.getCarYear() == Integer.parseInt(request.getNewYear())) {
                    carOrder.setDateOfOrder(Date.valueOf(String.valueOf(LocalDate.now())));
                    carOrdersService.save(carOrder);
                    return ResponseEntity.ok(new ModifyCarOrderResponse("Same order is made and the period is extended.",
                            carOrder.getCarManufacturer(),
                            carOrder.getCarModel(),
                            String.valueOf(carOrder.getCarYear()),
                            String.valueOf(carOrder.getDateOfOrder()),
                            carOrder.getOrderStatus()));
                } else {
                    List<CarOrdersEntity> carOrders = carOrdersService.findByUser_Id(userId);
                    if (carOrders.stream().noneMatch(carOrder1 ->
                            carOrder1.getCarManufacturer().equals(request.getNewManufacturer()) &&
                                    carOrder1.getCarModel().equals(request.getNewModel()) &&
                                    carOrder1.getCarYear() == Integer.parseInt(request.getNewYear()))) {

                        carOrder.setCarModel(request.getNewModel());
                        carOrder.setCarManufacturer(request.getNewManufacturer());
                        carOrder.setCarYear(Integer.parseInt(request.getNewYear()));
                        carOrder.setDateOfOrder(Date.valueOf(String.valueOf(LocalDate.now())));
                        checkOrderStatus(carOrdersService,carOrder);
                        carOrdersService.save(carOrder);

                        return ResponseEntity.ok(new ModifyCarOrderResponse("New order is made and the period is extended.",
                                carOrder.getCarManufacturer(),
                                carOrder.getCarModel(),
                                String.valueOf(carOrder.getCarYear()),
                                String.valueOf(carOrder.getDateOfOrder()),
                                carOrder.getOrderStatus()));
                    } else {
                        return ResponseEntity.ok(new ModifyCarOrderResponse("An order matching your request already exists.",
                                "",
                                "",
                                "",
                                "",
                                ""));
                    }
                }
            } else {
                return ResponseEntity.ok(new ModifyCarOrderResponse("Order is not found.",
                        "",
                        "",
                        "",
                        "",
                        ""));
            }
        } else {
            authenticationToken = authenticationTokenService.findByUser_Id(userId);
            if (userOptional.isPresent() && authenticationToken != null &&
                    replacedAuthTokensService.findByReplacedToken(request.getAuthToken()) != null) {

                Cookie cookie = new Cookie("authToken", authenticationToken.getToken());
                long maxAgeInSeconds = (authenticationToken.getExpireDate().getTime() - System.currentTimeMillis()) / 1000;
                cookie.setMaxAge((int) maxAgeInSeconds);
                cookie.setPath("/"); // Save the cookie for all pages of the site

                response.addCookie(cookie);
                if (carOrdersService.findByUser_IdAndCarManufacturerAndCarModelAndCarYear(userId,
                        request.getCurrentManufacturer(),
                        request.getCurrentModel(),
                        Integer.parseInt(request.getCurrentYear())).isPresent()) {

                    CarOrdersEntity carOrder = carOrdersService.findByUser_IdAndCarManufacturerAndCarModelAndCarYear(userId,
                            request.getCurrentManufacturer(),
                            request.getCurrentModel(),
                            Integer.parseInt(request.getCurrentYear())).get();

                    if (carOrder.getCarManufacturer().equals(request.getNewManufacturer()) &&
                            carOrder.getCarModel().equals(request.getNewModel()) &&
                            carOrder.getCarYear() == Integer.parseInt(request.getNewYear())) {
                        carOrder.setDateOfOrder(Date.valueOf(String.valueOf(LocalDate.now())));
                        carOrdersService.save(carOrder);
                        return ResponseEntity.ok(new ModifyCarOrderResponse("Same order is made and the period is extended.",
                                carOrder.getCarManufacturer(),
                                carOrder.getCarModel(),
                                String.valueOf(carOrder.getCarYear()),
                                String.valueOf(carOrder.getDateOfOrder()),
                                carOrder.getOrderStatus()));
                    } else {
                        List<CarOrdersEntity> carOrders = carOrdersService.findByUser_Id(userId);
                        if (carOrders.stream().noneMatch(carOrder1 ->
                                carOrder1.getCarManufacturer().equals(request.getNewManufacturer()) &&
                                        carOrder1.getCarModel().equals(request.getNewModel()) &&
                                        carOrder1.getCarYear() == Integer.parseInt(request.getNewYear()))) {

                            carOrder.setCarModel(request.getNewModel());
                            carOrder.setCarManufacturer(request.getNewManufacturer());
                            carOrder.setCarYear(Integer.parseInt(request.getNewYear()));
                            carOrder.setDateOfOrder(Date.valueOf(String.valueOf(LocalDate.now())));
                            carOrdersService.save(carOrder);

                            return ResponseEntity.ok(new ModifyCarOrderResponse("New order is made and the period is extended.",
                                    carOrder.getCarManufacturer(),
                                    carOrder.getCarModel(),
                                    String.valueOf(carOrder.getCarYear()),
                                    String.valueOf(carOrder.getDateOfOrder()),
                                    carOrder.getOrderStatus()));
                        } else {
                            return ResponseEntity.ok(new ModifyCarOrderResponse("An order matching your request already exists.",
                                    "",
                                    "",
                                    "",
                                    "",
                                    ""));
                        }
                    }
                } else {
                    return ResponseEntity.ok(new ModifyCarOrderResponse("Order is not found.",
                            "",
                            "",
                            "",
                            "",
                            ""));
                }
            }
            return ResponseEntity.ok(new ModifyCarOrderResponse("User is not found.",
                    "",
                    "",
                    "",
                    "",
                    ""));
        }
    }
}
