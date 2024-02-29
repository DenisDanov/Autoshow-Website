package com.example.app.dbData.orderCarService;

public class ModifyCarOrderResponse {
    private final String result;
    private final String carManufacturer;
    private final String carModel;
    private final String carYear;
    private final String dateOfOrder;
    private final String orderStatus;

    public ModifyCarOrderResponse(String result, String carManufacturer, String carModel, String carYear, String dateOfOrder, String orderStatus) {
        this.result = result;
        this.carManufacturer = carManufacturer;
        this.carModel = carModel;
        this.carYear = carYear;
        this.dateOfOrder = dateOfOrder;
        this.orderStatus = orderStatus;
    }

    public String getResult() {
        return result;
    }

    public String getCarManufacturer() {
        return carManufacturer;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getCarYear() {
        return carYear;
    }

    public String getDateOfOrder() {
        return dateOfOrder;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
