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
import java.util.Collections;
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
        if (make.equals("mercedes-benz")) {
            if (model.isEmpty()) {
                return addNewMercedesModels(responseEntity);
            } else if (model.equals("Maybach GLS 600") ||
            model.equals("Brabus G900") || model.equals("Brabus 800 S63")){
                return addNewMercedesModelsYears(responseEntity, model);
            }
        } else if (make.equals("mclaren")) {
            if (model.isEmpty()) {
                return addNewMclarenModels(responseEntity);
            } else if (model.equals("Senna")){
                return addNewMclarenModelsYears(responseEntity, model);
            }
        } else if (make.equals("porsche")) {
            if (model.isEmpty()) {
                return addNewPorscheModels(responseEntity);
            } else if (model.equals("GT3 RS")){
                return addNewPorscheModelsYears(responseEntity, model);
            }
        } else if (make.equals("rolls-royce")) {
            if (!model.isEmpty()) {
                return addNewRollsRoyceModelsYears(responseEntity, model);
            }
        }else if (make.equals("nissan")) {
            if (model.isEmpty()) {
                return addNewNissanModels(responseEntity);
            } else if (model.equals("GT-R R33") || model.equals("Silvia-180SX")){
                return addNewNissanModelsYears(responseEntity, model);
            }
        }
        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(responseEntity.getBody());
    }

    private ResponseEntity<String> addNewNissanModelsYears(ResponseEntity<String> responseEntity, String model) {
        List<String> modelsFromAPI = new ArrayList<>();
        // Add your custom model
        if (model.equals("GT-R R33")) {
            modelsFromAPI.add("1995");
        } else if (model.equals("Silvia-180SX")) {
            modelsFromAPI.add("1996");
        }

        // Prepare the modified response
        String modifiedResponse = createModifiedResponseYears(modelsFromAPI);
        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(modifiedResponse);
    }

    private ResponseEntity<String> addNewNissanModels(ResponseEntity<String> responseEntity) {
        String responseBody = responseEntity.getBody();
        List<String> modelsFromAPI = extractModelsFromResponse(responseBody);

        modelsFromAPI.add("GT-R R33");
        modelsFromAPI.add("Silvia-180SX");
        Collections.sort(modelsFromAPI);

        // Prepare the modified response
        String modifiedResponse = createModifiedResponse(modelsFromAPI);
        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(modifiedResponse);
    }

    private ResponseEntity<String> addNewRollsRoyceModelsYears(ResponseEntity<String> responseEntity, String model) {
        List<String> modelsFromAPI = extractModelsYearsFromResponse(responseEntity.getBody());
        // Add your custom model
        if (model.equals("Ghost")) {
            modelsFromAPI.add(0,"2022");
        }

        // Prepare the modified response
        String modifiedResponse = createModifiedResponseYears(modelsFromAPI);
        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(modifiedResponse);
    }

    private ResponseEntity<String> addNewPorscheModelsYears(ResponseEntity<String> responseEntity, String model) {
        List<String> modelsFromAPI = new ArrayList<>();
        // Add your custom model
        if (model.equals("GT3 RS")) {
            modelsFromAPI.add("2023");
        }

        // Prepare the modified response
        String modifiedResponse = createModifiedResponseYears(modelsFromAPI);
        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(modifiedResponse);
    }

    private ResponseEntity<String> addNewPorscheModels(ResponseEntity<String> responseEntity) {
        String responseBody = responseEntity.getBody();
        List<String> modelsFromAPI = extractModelsFromResponse(responseBody);

        modelsFromAPI.add("GT3 RS");
        Collections.sort(modelsFromAPI);

        // Prepare the modified response
        String modifiedResponse = createModifiedResponse(modelsFromAPI);
        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(modifiedResponse);
    }

    private ResponseEntity<String> addNewMclarenModelsYears(ResponseEntity<String> responseEntity, String model) {
        List<String> modelsFromAPI = new ArrayList<>();
        // Add your custom model
        if (model.equals("Senna")) {
            modelsFromAPI.add("2022");
        }

        // Prepare the modified response
        String modifiedResponse = createModifiedResponseYears(modelsFromAPI);
        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(modifiedResponse);
    }

    private ResponseEntity<String> addNewMclarenModels(ResponseEntity<String> responseEntity) {
        String responseBody = responseEntity.getBody();
        List<String> modelsFromAPI = extractModelsFromResponse(responseBody);

        modelsFromAPI.add("Senna");
        Collections.sort(modelsFromAPI);

        // Prepare the modified response
        String modifiedResponse = createModifiedResponse(modelsFromAPI);
        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(modifiedResponse);
    }

    private ResponseEntity<String> addNewMercedesModelsYears(ResponseEntity<String> responseEntity,
                                                             String model) {
        List<String> modelsFromAPI = new ArrayList<>();
        // Add your custom model
        switch (model) {
            case "Maybach GLS 600", "Brabus G900":
                modelsFromAPI.add("2023");
                break;
            case "Brabus 800 S63":
                modelsFromAPI.add("2022");
                break;
        }

        // Prepare the modified response
        String modifiedResponse = createModifiedResponseYears(modelsFromAPI);
        return ResponseEntity.status(responseEntity.getStatusCode())
                .headers(responseEntity.getHeaders())
                .body(modifiedResponse);
    }

    private ResponseEntity<String> addNewMercedesModels(ResponseEntity<String> responseEntity) {
        String responseBody = responseEntity.getBody();
        List<String> modelsFromAPI = extractModelsFromResponse(responseBody);

        modelsFromAPI.add("Brabus G900");
        modelsFromAPI.add("Maybach GLS 600");
        modelsFromAPI.add("Brabus 800 S63");
        Collections.sort(modelsFromAPI);

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

    private List<String> extractModelsYearsFromResponse(String responseBody) {
        List<String> modelsYears = new ArrayList<>();
        // Extract models from the response
        try {
            String jsonString = responseBody.substring(responseBody.indexOf('(') + 1, responseBody.lastIndexOf(')'));
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray modelsArray = jsonObject.getJSONArray("Trims");
            for (int i = 0; i < modelsArray.length(); i++) {
                JSONObject modelObject = modelsArray.getJSONObject(i);
                String modelName = modelObject.getString("model_year");
                modelsYears.add(modelName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelsYears;
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

    private String createModifiedResponseYears(List<String> models) {
        JSONObject jsonObject = new JSONObject();
        JSONArray modelsArray = new JSONArray();
        for (String model : models) {
            JSONObject modelObject = new JSONObject();
            modelObject.put("model_year", model);
            modelsArray.put(modelObject);
        }
        jsonObject.put("Trims", modelsArray);
        return "?(" + jsonObject.toString() + ")";
    }
}
