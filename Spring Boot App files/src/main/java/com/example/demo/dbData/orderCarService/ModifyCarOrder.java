package com.example.demo.dbData.orderCarService;

public class ModifyCarOrder {
    private String id;
    private String currentManufacturer;
    private String currentModel;
    private String currentYear;
    private String newManufacturer;
    private String newModel;
    private String newYear;
    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public String getId() {
        return id;
    }

    public String getCurrentManufacturer() {
        return currentManufacturer;
    }

    public String getCurrentModel() {
        return currentModel;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public String getNewManufacturer() {
        return newManufacturer;
    }

    public String getNewModel() {
        return newModel;
    }

    public String getNewYear() {
        return newYear;
    }
}
