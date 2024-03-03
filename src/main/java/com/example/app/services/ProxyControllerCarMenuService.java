package com.example.app.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface ProxyControllerCarMenuService {

    ResponseEntity<String> proxyCarSpecs(@RequestParam String make, @RequestParam String model);

}
