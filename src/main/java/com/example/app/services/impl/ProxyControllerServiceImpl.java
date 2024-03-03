package com.example.app.services.impl;

import com.example.app.services.ProxyControllerService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Service
public class ProxyControllerServiceImpl implements ProxyControllerService {

    private final RestTemplate restTemplate;

    public ProxyControllerServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<String> proxyCarSpecs(String vin) {
        Properties properties = new Properties();
        Resource resource = new ClassPathResource("application.properties");
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
