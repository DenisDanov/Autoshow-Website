package com.example.app.services;

import com.example.app.data.DTOs.CarOrderSpecDataDTO;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface ProxyControllerCarMenuService {

    ResponseEntity<String> proxyCarSpecs(@RequestParam String make, @RequestParam String model);

    JSONObject proxyCarTrims(@RequestParam String make, @RequestParam String model, @RequestParam int year);

    ResponseEntity<CarOrderSpecDataDTO> proxyCarData(@RequestParam String make, @RequestParam String model, @RequestParam int year);

}
