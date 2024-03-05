package com.example.app.services.impl;

import com.example.app.services.ProxyControllerCarMenuService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProxyControllerCarMenuServiceImpl implements ProxyControllerCarMenuService {

    private final RestTemplate restTemplate;

    public ProxyControllerCarMenuServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<String> proxyCarSpecs(String make, String model) {
        // If make is empty, return the local JSON file
        if (Objects.equals(make, "")) {
            try {
                // Load the local JSON file from BOOT-INF/classes
                Resource resource = new ClassPathResource("carManufactures.json");
                InputStream inputStream = resource.getInputStream();
                String jsonString = new BufferedReader(new InputStreamReader(inputStream))
                        .lines().collect(Collectors.joining("\n"));
                return ResponseEntity.ok(jsonString);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading local JSON file");
            }
        }

        // Logic for making external API calls
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

        // Get models from the external API response
        String responseBody = responseEntity.getBody();
        List<String> modelsFromAPI = extractModelsFromResponse(responseBody);

        // Add your custom model
        String customModel = "Custom Model"; // Replace this with your custom model
        modelsFromAPI.add(0, customModel); // Add custom model at the beginning of the list

        // Prepare the modified response
        String modifiedResponse = createModifiedResponse(modelsFromAPI);

        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(modifiedResponse);
    }

    // Helper method to extract models from the API response
    private List<String> extractModelsFromResponse(String responseBody) {
        List<String> models = new ArrayList<>();
        // Extract models from the response
        try {
            String jsonString = responseBody.substring(responseBody.indexOf('(') + 1, responseBody.lastIndexOf(')'));
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray modelsArray = jsonObject.getJSONArray("Models");
            for (int i = 0; i < modelsArray.length(); i++) {
                JSONObject modelObject = modelsArray.getJSONObject(i);
                String modelName = modelObject.getString("model_name");
                models.add(modelName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return models;
    }

    // Helper method to create a modified response with custom model added
    private String createModifiedResponse(List<String> models) {
        JSONObject jsonObject = new JSONObject();
        JSONArray modelsArray = new JSONArray();
        for (String model : models) {
            JSONObject modelObject = new JSONObject();
            modelObject.put("model_name", model);
            modelsArray.put(modelObject);
        }
        jsonObject.put("Models", modelsArray);
        return "?(" + jsonObject.toString() + ")";
    }


}
