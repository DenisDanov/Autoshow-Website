package com.example.app.services;

import com.example.app.data.DTOs.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CarOrderControllerService {

    ResponseEntity<String> addOrder(@RequestBody CarOrderRequest request,
                                    HttpServletResponse response);

    ResponseEntity<List<CarOrder>> getOrders(@RequestParam("id") Long userId,
                                             HttpServletResponse response);

    ResponseEntity<String> removeCarOrder(@RequestBody RemoveCarOrderRequest request,
                                          HttpServletResponse response);

    ResponseEntity<ModifyCarOrderResponse> modifyCarOrder(@RequestBody ModifyCarOrder request,
                                                          HttpServletResponse response);
}
