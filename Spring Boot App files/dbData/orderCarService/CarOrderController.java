package com.example.demo.dbData.orderCarService;

import com.example.demo.dbData.FavoriteRequest;
import com.example.demo.dbData.FavoriteResponse;
import com.example.demo.dbData.User;
import com.example.demo.dbData.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

            if (user.getCarOrders() != null && !user.getCarOrders().contains(carOrder)) {
                user.getCarOrders().add(carOrder);
            } else {
                user.setCarOrders(new ArrayList<>());
            }

            // Save the updated user
            userRepository.save(user);

            return ResponseEntity.ok("Order sent successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @GetMapping("/get")
    public ResponseEntity<List<CarOrder>> getOrders (@RequestBody CarOrderGetRequest request) {
        // Find the user by ID
        Long userId = Long.parseLong(request.getId());
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(user.getCarOrders());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
