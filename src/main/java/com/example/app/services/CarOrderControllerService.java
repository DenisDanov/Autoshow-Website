package com.example.app.services;

import com.example.app.data.DTOs.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CarOrderControllerService {

    ResponseEntity<String> addOrder(@RequestBody CarOrderRequestDTO request,
                                    HttpServletResponse response);

    ResponseEntity<List<CarOrderDTO>> getOrders(@RequestParam("id") Long userId,
                                                @RequestParam("authToken") String authToken,
                                                HttpServletResponse response);

    ResponseEntity<String> removeCarOrder(@RequestBody RemoveCarOrderRequestDTO request,
                                          HttpServletResponse response);

    ResponseEntity<ModifyCarOrderResponseDTO> modifyCarOrder(@RequestBody ModifyCarOrderDTO request,
                                                             HttpServletResponse response);
}
