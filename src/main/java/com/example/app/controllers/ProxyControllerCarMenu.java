package com.example.app.controllers;

import com.example.app.services.ProxyControllerCarMenuService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/proxy-carMenu")
public class ProxyControllerCarMenu {

    private final ProxyControllerCarMenuService proxyControllerCarMenuService;

    public ProxyControllerCarMenu(ProxyControllerCarMenuService proxyControllerCarMenuService) {
        this.proxyControllerCarMenuService = proxyControllerCarMenuService;
    }

    @GetMapping("/carquery-api")
    public ResponseEntity<String> proxyCarSpecs(@RequestParam String make, @RequestParam String model) {
       return this.proxyControllerCarMenuService.proxyCarSpecs(make,model);
    }
}
