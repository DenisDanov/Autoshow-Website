package com.example.app.api;

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

    private final RestTemplate restTemplate;

    public ProxyControllerCarMenu(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/carquery-api")
    public ResponseEntity<String> proxyCarSpecs(@RequestParam String make, @RequestParam String model) {
        // If make is empty, return the local JSON file
        if (Objects.equals(make, "")) {
            try {
                // Load the local JSON file from BOOT-INF/classes
                Resource resource = new ClassPathResource("BOOT-INF/classes/carManufactures.json");
                InputStream inputStream = resource.getInputStream();
                String jsonString = new BufferedReader(new InputStreamReader(inputStream))
                        .lines().collect(Collectors.joining("\n"));
                return ResponseEntity.ok(jsonString);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading local JSON file");
            }
        }

        // logic for making external API calls
        String apiUrl = "";
        if (Objects.equals(model, "")) {
            apiUrl = "https://www.carqueryapi.com/api/0.3/?callback=?&cmd=getModels&make=" + make;
        } else {
            apiUrl = "https://www.carqueryapi.com/api/0.3/?callback=?&cmd=getTrims&make=" + make + "&model=" + model;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "danovs-autoshow");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

        // Forward the response to the client-side JavaScript code (Cars-Data.js)
        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(responseEntity.getBody());
    }
}
