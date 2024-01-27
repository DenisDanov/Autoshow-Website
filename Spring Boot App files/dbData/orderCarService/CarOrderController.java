package com.example.demo.dbData.orderCarService;

import com.example.demo.dbData.User;
import com.example.demo.dbData.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carOrders")
public class CarOrderController {

    private final UserRepository userRepository;

    private final CarOrdersRepository carOrdersRepository;

    @Autowired
    public CarOrderController(UserRepository userRepository, CarOrdersRepository carOrdersRepository) {
        this.userRepository = userRepository;
        this.carOrdersRepository = carOrdersRepository;
    }

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

            List<CarOrdersEntity> carOrders = carOrdersRepository.findByUser_Id(userId);
            if (!carOrders.isEmpty()) {
                if (carOrders.stream().noneMatch(carOrder1 ->
                        carOrder1.getCarManufacturer().equals(carOrder.getCarManufacturer()) &&
                                carOrder1.getCarModel().equals(carOrder.getCarModel()) &&
                                carOrder1.getCarYear() == (Integer.parseInt(carOrder.getCarYear())))) {
                    carOrdersRepository.save(new CarOrdersEntity(user, carOrder.getCarManufacturer(),
                            carOrder.getCarModel(), Integer.parseInt(carOrder.getCarYear()), carOrder.getOrderStatus(),
                            carOrder.getDateOfOrder()));
                    userRepository.save(user);
                    return ResponseEntity.ok("Order sent successfully.");
                } else {
                    return ResponseEntity.ok("Car order already exists.");
                }
            } else {
                carOrdersRepository.save(new CarOrdersEntity(user, carOrder.getCarManufacturer(),
                        carOrder.getCarModel(), Integer.parseInt(carOrder.getCarYear()), carOrder.getOrderStatus(),
                        carOrder.getDateOfOrder()));
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
            List<CarOrder> getAllOrders = carOrdersRepository
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

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeCarOrder(@RequestBody RemoveCarOrderRequest request) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getId());
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (carOrdersRepository.deleteCarOrder(request.getCarManufacturer(),
                    userId,
                    request.getCarModel(),
                    request.getCarYear()) > 0) {
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
            if (carOrdersRepository.findByUser_IdAndCarManufacturerAndCarModelAndCarYear(userId,
                    request.getCurrentManufacturer(),
                    request.getCurrentModel(),
                    Integer.parseInt(request.getCurrentYear())).isPresent()) {

                CarOrdersEntity carOrder = carOrdersRepository.findByUser_IdAndCarManufacturerAndCarModelAndCarYear(userId,
                        request.getCurrentManufacturer(),
                        request.getCurrentModel(),
                        Integer.parseInt(request.getCurrentYear())).get();

                if (carOrder.getCarManufacturer().equals(request.getNewManufacturer()) &&
                        carOrder.getCarModel().equals(request.getNewModel()) &&
                        carOrder.getCarYear() == Integer.parseInt(request.getNewYear())) {
                    carOrder.setDateOfOrder(Date.valueOf(String.valueOf(LocalDate.now())));
                    carOrdersRepository.save(carOrder);
                    return ResponseEntity.ok(new ModifyCarOrderResponse("Same order is made and the period is extended.",
                            carOrder.getCarManufacturer(),
                            carOrder.getCarModel(),
                            String.valueOf(carOrder.getCarYear()),
                            String.valueOf(carOrder.getDateOfOrder()),
                            carOrder.getOrderStatus()));
                } else {
                    List<CarOrdersEntity> carOrders = carOrdersRepository.findByUser_Id(userId);
                    if (carOrders.stream().noneMatch(carOrder1 ->
                            carOrder1.getCarManufacturer().equals(request.getNewManufacturer()) &&
                                    carOrder1.getCarModel().equals(request.getNewModel()) &&
                                    carOrder1.getCarYear() == Integer.parseInt(request.getNewYear()))) {

                        carOrder.setCarModel(request.getNewModel());
                        carOrder.setCarManufacturer(request.getNewManufacturer());
                        carOrder.setCarYear(Integer.parseInt(request.getNewYear()));
                        carOrder.setDateOfOrder(Date.valueOf(String.valueOf(LocalDate.now())));
                        carOrdersRepository.save(carOrder);

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
            return ResponseEntity.ok(new ModifyCarOrderResponse("User is not found.",
                    "",
                    "",
                    "",
                    "",
                    ""));
        }
    }
}
