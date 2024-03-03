package com.example.app.controllers;

import com.example.app.services.ProxyControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@RequestMapping("/api/proxy")
public class ProxyController {

    private final ProxyControllerService proxyControllerService;

    @Autowired
    public ProxyController(ProxyControllerService proxyControllerService) {
        this.proxyControllerService = proxyControllerService;
    }

    @GetMapping("/car-specs")
    public ResponseEntity<String> proxyCarSpecs(@RequestParam String vin) {
        return proxyControllerService.proxyCarSpecs(vin);
    }

}
