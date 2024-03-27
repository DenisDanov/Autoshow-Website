package com.example.app.controllers;

import com.example.app.data.DTOs.*;
import com.example.app.services.CarOrderControllerService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carOrders")
public class CarOrderController {

    private final CarOrderControllerService carOrderControllerService;

    public CarOrderController(CarOrderControllerService carOrderControllerService) {
        this.carOrderControllerService = carOrderControllerService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addOrder(@RequestBody CarOrderRequest request,
                                           HttpServletResponse response) {
        return carOrderControllerService.addOrder(request, response);
    }

    @GetMapping("/get")
    public ResponseEntity<List<CarOrder>> getOrders(@RequestParam("id") Long userId,
                                                    @RequestParam("authToken") String authToken,
                                                    HttpServletResponse response) {
        return carOrderControllerService.getOrders(userId, authToken, response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeCarOrder(@RequestBody RemoveCarOrderRequest request,
                                                 HttpServletResponse response) {
        return carOrderControllerService.removeCarOrder(request, response);
    }

    @PutMapping("/modify")
    public ResponseEntity<ModifyCarOrderResponse> modifyCarOrder(@RequestBody ModifyCarOrder request,
                                                                 HttpServletResponse response) {
        return carOrderControllerService.modifyCarOrder(request, response);
    }
}
