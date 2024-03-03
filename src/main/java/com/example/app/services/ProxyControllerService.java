package com.example.app.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface ProxyControllerService {

    ResponseEntity<String> proxyCarSpecs(@RequestParam String vin);

}
