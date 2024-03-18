package com.example.app.controllers;

import com.example.app.data.DTOs.CarOrderSpecDataDTO;
import com.example.app.services.ProxyControllerCarMenuService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/proxy-carMenu")
public class ProxyControllerCarMenu {

    @Autowired
    private final ProxyControllerCarMenuService proxyControllerCarMenuService;

    public ProxyControllerCarMenu(ProxyControllerCarMenuService proxyControllerCarMenuService) {
        this.proxyControllerCarMenuService = proxyControllerCarMenuService;
    }

    @GetMapping("/carquery-api")
    public ResponseEntity<String> proxyCarSpecs(@RequestParam String make, @RequestParam String model) {
       return this.proxyControllerCarMenuService.proxyCarSpecs(make,model);
    }

    @GetMapping("/carquery-trims")
    public JSONObject proxyCarSpecs(@RequestParam String make, @RequestParam String model, @RequestParam int year) {
        return proxyControllerCarMenuService.proxyCarTrims(make,model,year);
    }

    @GetMapping("/carquery-car-data")
    public ResponseEntity<CarOrderSpecDataDTO> proxyCardData(@RequestParam String make, @RequestParam String model, @RequestParam int year) {
        return proxyControllerCarMenuService.proxyCarData(make,model,year);
    }
}
