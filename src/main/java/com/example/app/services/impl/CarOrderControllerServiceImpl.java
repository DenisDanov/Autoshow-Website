package com.example.app.services.impl;

import com.example.app.data.DTOs.*;
import com.example.app.data.entities.AuthenticationToken;
import com.example.app.data.entities.CarOrdersEntity;
import com.example.app.data.entities.User;
import com.example.app.services.*;
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

    public CarOrderControllerServiceImpl(UserService userService,
                                         CarOrdersServiceImpl carOrdersService,
                                         AuthenticationTokenService authenticationTokenService) {
        this.userService = userService;
        this.carOrdersService = carOrdersService;
        this.authenticationTokenService = authenticationTokenService;
    }

    @Override
    public ResponseEntity<String> addOrder(CarOrderRequestDTO request, HttpServletResponse response) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getId());
        Optional<User> userOptional = userService.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokenService.findByToken(request.getAuthToken());
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authenticationToken.getUser().getId(), userOptional.get().getId())) {
            User user = userOptional.get();

            List<CarOrdersEntity> carOrders = carOrdersService.findByUser_Id(userId);
            if (!carOrders.isEmpty()) {
                if (carOrders.stream().noneMatch(carOrder1 ->
                        carOrder1.getCarManufacturer().equals(request.getCarManufacturer()) &&
                                carOrder1.getCarModel().equals(request.getCarModel()) &&
                                carOrder1.getCarYear() == (Integer.parseInt(request.getCarYear())))) {
                    carOrdersService.save(new CarOrdersEntity(user, request.getCarManufacturer(),
                            request.getCarModel(), Integer.parseInt(request.getCarYear())));
                    userService.save(user);
                    return ResponseEntity.ok("Order sent successfully.");
                } else {
                    return ResponseEntity.ok("Car order already exists.");
                }
            } else {
                carOrdersService.save(new CarOrdersEntity(user, request.getCarManufacturer(),
                        request.getCarModel(), Integer.parseInt(request.getCarYear())));
                return ResponseEntity.ok("Order sent successfully.");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    @Override
    public ResponseEntity<List<CarOrderDTO>> getOrders(Long userId, String authToken, HttpServletResponse response) {
        // Find the user by ID
        Optional<User> userOptional = userService.findById(userId);
        AuthenticationToken authenticationToken = authenticationTokenService.findByUser_Id(userId);
        if (userOptional.isPresent() && authenticationToken != null &&
                Objects.equals(authToken, authenticationToken.getToken())) {
            List<CarOrderDTO> getAllOrders = carOrdersService
                    .findByUser_Id(userId)
                    .stream()
                    .map(carOrder -> new CarOrderDTO(
                            carOrder.getCarManufacturer(),
                            carOrder.getCarModel(),
                            String.valueOf(carOrder.getCarYear()),
                            carOrder.getOrderStatus(),
                            carOrder.getDateOfOrder()))
                    .toList();
            return ResponseEntity.ok(getAllOrders);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Override
    public ResponseEntity<String> removeCarOrder(RemoveCarOrderRequestDTO request, HttpServletResponse response) {
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
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    @Override
    public ResponseEntity<ModifyCarOrderResponseDTO> modifyCarOrder(ModifyCarOrderDTO request, HttpServletResponse response) {
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
                    return ResponseEntity.ok(new ModifyCarOrderResponseDTO("Same order is made and the period is extended.",
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
                        checkOrderStatus(carOrdersService, carOrder);
                        carOrdersService.save(carOrder);

                        return ResponseEntity.ok(new ModifyCarOrderResponseDTO("New order is made and the period is extended.",
                                carOrder.getCarManufacturer(),
                                carOrder.getCarModel(),
                                String.valueOf(carOrder.getCarYear()),
                                String.valueOf(carOrder.getDateOfOrder()),
                                carOrder.getOrderStatus()));
                    } else {
                        return ResponseEntity.ok(new ModifyCarOrderResponseDTO("An order matching your request already exists.",
                                "",
                                "",
                                "",
                                "",
                                ""));
                    }
                }
            } else {
                return ResponseEntity.ok(new ModifyCarOrderResponseDTO("Order is not found.",
                        "",
                        "",
                        "",
                        "",
                        ""));
            }
        }
        return ResponseEntity.ok(new ModifyCarOrderResponseDTO("User is not found.",
                "",
                "",
                "",
                "",
                ""));
    }
}
