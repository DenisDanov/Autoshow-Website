package com.example.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.Properties;

@RestController
@Controller
@RequestMapping("/api/proxy")
public class ProxyController {

    private final RestTemplate restTemplate;

    @Autowired
    public ProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/car-specs")
    public ResponseEntity<String> proxyCarSpecs(@RequestParam String vin) {
        Properties properties = new Properties();
        Resource resource = new ClassPathResource("BOOT-INF/classes/application.properties");
        try (InputStream input = resource.getInputStream()) {
            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
                return ResponseEntity.ok("Sorry, unable to find application.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // URL of the external API
        String externalApiUrl = "https://api.carsxe.com/specs";
        // additional parameters needed for the API call
        String apiKey = properties.getProperty("external.api.key");

        // URL for the external API
        String apiUrl = externalApiUrl + "?key=" + apiKey + "&deepdata=1&vin=" + vin;

        // API call to the external API
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);

        // Forward the response to the client side JavaScript code(Cars-Data.js)
        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(responseEntity.getBody());
    }

}
