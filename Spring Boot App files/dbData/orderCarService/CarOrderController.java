package com.example.demo.dbData.orderCarService;

import com.example.demo.dbData.User;
import com.example.demo.dbData.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carOrders")
public class CarOrderController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addOrder(@RequestBody CarOrderRequest request) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getId());
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            CarOrder carOrder = new CarOrder(request.getCarManufacturer(),
                    request.getCarModel(),
                    request.getCarYear());

            if (user.getCarOrders() != null) {
                if (user.getCarOrders().stream().noneMatch(carOrder1 ->
                        carOrder1.getCarManufacturer().equals(carOrder.getCarManufacturer()) &&
                                carOrder1.getCarModel().equals(carOrder.getCarModel()) &&
                                carOrder1.getCarYear().equals(carOrder.getCarYear()))) {
                    user.getCarOrders().add(carOrder);
                    userRepository.save(user);
                    return ResponseEntity.ok("Order sent successfully.");
                } else {
                    return ResponseEntity.ok("Car order already exists.");
                }
            } else {
                user.setCarOrders(new ArrayList<>());
                user.getCarOrders().add(carOrder);
                userRepository.save(user);
                return ResponseEntity.ok("Order sent successfully.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<CarOrder>> getOrders(@RequestParam("id") Long userId) {
        // Find the user by ID
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(user.getCarOrders());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeCarOrder(@RequestBody RemoveCarOrderRequest request) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getId());
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getCarOrders().removeIf(carOrder ->
                    carOrder.getCarManufacturer().equals(request.getCarManufacturer()) &&
                            carOrder.getCarModel().equals(request.getCarModel()) &&
                            carOrder.getCarYear().equals(request.getCarYear()))) {
                userRepository.save(user);
                return ResponseEntity.ok("Car order successfully removed.");
            } else {
                return ResponseEntity.ok("Car order doesn't exist.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @PutMapping("/modify")
    public ResponseEntity<ModifyCarOrderResponse> modifyCarOrder(@RequestBody ModifyCarOrder request) {
        Long userId = Long.parseLong(request.getId());
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            for (CarOrder carOrder : user.getCarOrders()) {
                if (carOrder.getCarManufacturer().equals(request.getCurrentManufacturer()) &&
                        carOrder.getCarModel().equals(request.getCurrentModel()) &&
                        carOrder.getCarYear().equals(request.getCurrentYear())) {

                    if (carOrder.getCarManufacturer().equals(request.getNewManufacturer()) &&
                            carOrder.getCarModel().equals(request.getNewModel()) &&
                            carOrder.getCarYear().equals(request.getNewYear())) {
                        carOrder.setDateOfOrder(String.valueOf(LocalDate.now()));
                        userRepository.save(user);
                        return ResponseEntity.ok(new ModifyCarOrderResponse("Same order is made and the period is extended.",
                                carOrder.getCarManufacturer(),
                                carOrder.getCarModel(),
                                carOrder.getCarYear(),
                                carOrder.getDateOfOrder(),
                                carOrder.getOrderStatus()));
                    } else {
                        if (user.getCarOrders().stream().noneMatch(carOrder1 ->
                                carOrder1.getCarManufacturer().equals(request.getNewManufacturer()) &&
                                        carOrder1.getCarModel().equals(request.getNewModel()) &&
                                        carOrder1.getCarYear().equals(request.getNewYear()))) {
                            carOrder.setCarModel(request.getNewModel());
                            carOrder.setCarManufacturer(request.getNewManufacturer());
                            carOrder.setCarYear(request.getNewYear());
                            carOrder.setDateOfOrder(String.valueOf(LocalDate.now()));
                            userRepository.save(user);

                            return ResponseEntity.ok(new ModifyCarOrderResponse("New order is made and the period is extended.",
                                    carOrder.getCarManufacturer(),
                                    carOrder.getCarModel(),
                                    carOrder.getCarYear(),
                                    carOrder.getDateOfOrder(),
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
                }
            }
            return ResponseEntity.ok(new ModifyCarOrderResponse("Order is not found.",
                    "",
                    "",
                    "",
                    "",
                    ""));
        } else {
            return ResponseEntity.ok(new ModifyCarOrderResponse("User is not found.",
                    "",
                    "",
                    "",
                    "",
                    ""));
        }
    }
}
