package com.example.app.data.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CarOrderSpecDataDTO {

    @JsonProperty("model_trim")
    private String modelTrim;

    @JsonProperty("make_display")
    private String makeDisplay;

    @JsonProperty("model_engine_cc")
    private Integer modelEngineCc;

    @JsonProperty("model_engine_power_rpm")
    private Integer modelEnginePowerRpm;

    @JsonProperty("model_body")
    private String modelBody;

    @JsonProperty("model_drive")
    private String modelDrive;

    @JsonProperty("model_top_speed_kph")
    private Double modelTopSpeedKph;

    @JsonProperty("model_doors")
    private Integer modelDoors;

    @JsonProperty("model_name")
    private String modelName;

    @JsonProperty("model_co2")
    private Double modelCo2;

    @JsonProperty("model_lkm_mixed")
    private Double modelLkmMixed;

    @JsonProperty("model_lkm_hwy")
    private Double modelLkmHwy;

    @JsonProperty("model_engine_type")
    private String modelEngineType;

    @JsonProperty("make_country")
    private String makeCountry;

    @JsonProperty("model_engine_torque_rpm")
    private Double modelEngineTorqueRpm;

    @JsonProperty("model_engine_fuel")
    private String modelEngineFuel;

    @JsonProperty("model_engine_torque_nm")
    private Integer modelEngineTorqueNm;

    @JsonProperty("model_year")
    private Integer modelYear;

    @JsonProperty("model_0_to_100_kph")
    private Double model0To100Kph;

    @JsonProperty("model_engine_position")
    private String modelEnginePosition;

    @JsonProperty("model_transmission_type")
    private String modelTransmissionType;

    @JsonProperty("model_fuel_cap_l")
    private Double modelFuelCapL;

    @JsonProperty("model_engine_valves_per_cyl")
    private Integer modelEngineValvesPerCyl;

    @JsonProperty("model_engine_power_ps")
    private Integer modelEnginePowerPs;

    @JsonProperty("model_weight_kg")
    private Double modelWeightKg;

    @JsonProperty("model_engine_cyl")
    private String modelEngineCyl;

    @JsonProperty("model_seats")
    private Integer modelSeats;

    @JsonProperty("model_lkm_city")
    private Double modelLkmCity;

    @JsonProperty("model_engine_compression")
    private String modelEngineCompression;

    @JsonProperty("model_horsepower")
    private Integer modelEngineHorsePower;

    // Additional specifications
    @JsonProperty("battery_capacity_kwh")
    private Double batteryCapacityKwh;

    @JsonProperty("range_km")
    private Double rangeKm;

    @JsonProperty("model_wheelbase_mm")
    private Integer wheelbaseMm;

    @JsonProperty("model_length_mm")
    private Integer lengthMm;

    @JsonProperty("model_width_mm")
    private Integer widthMm;

    @JsonProperty("model_height_mm")
    private Integer heightMm;

    @JsonProperty("cargo_capacity_l")
    private Double cargoCapacityL;

    @JsonProperty("ground_clearance_mm")
    private Integer groundClearanceMm;

    @JsonProperty("max_charging_power_kw")
    private Integer maxChargingPowerKw;

    @JsonProperty("charging_time_hours")
    private Double chargingTimeHours;

    public CarOrderSpecDataDTO() {
    }

    public Double getBatteryCapacityKwh() {
        return batteryCapacityKwh;
    }

    public void setBatteryCapacityKwh(Double batteryCapacityKwh) {
        this.batteryCapacityKwh = batteryCapacityKwh;
    }

    public Double getRangeKm() {
        return rangeKm;
    }

    public void setRangeKm(Double rangeKm) {
        this.rangeKm = rangeKm;
    }

    public Integer getWheelbaseMm() {
        return wheelbaseMm;
    }

    public void setWheelbaseMm(Integer wheelbaseMm) {
        this.wheelbaseMm = wheelbaseMm;
    }

    public Integer getLengthMm() {
        return lengthMm;
    }

    public void setLengthMm(Integer lengthMm) {
        this.lengthMm = lengthMm;
    }

    public Integer getWidthMm() {
        return widthMm;
    }

    public void setWidthMm(Integer widthMm) {
        this.widthMm = widthMm;
    }

    public Integer getHeightMm() {
        return heightMm;
    }

    public void setHeightMm(Integer heightMm) {
        this.heightMm = heightMm;
    }

    public Double getCargoCapacityL() {
        return cargoCapacityL;
    }

    public void setCargoCapacityL(Double cargoCapacityL) {
        this.cargoCapacityL = cargoCapacityL;
    }

    public Integer getGroundClearanceMm() {
        return groundClearanceMm;
    }

    public void setGroundClearanceMm(Integer groundClearanceMm) {
        this.groundClearanceMm = groundClearanceMm;
    }

    public Integer getMaxChargingPowerKw() {
        return maxChargingPowerKw;
    }

    public void setMaxChargingPowerKw(Integer maxChargingPowerKw) {
        this.maxChargingPowerKw = maxChargingPowerKw;
    }

    public Double getChargingTimeHours() {
        return chargingTimeHours;
    }

    public void setChargingTimeHours(Double chargingTimeHours) {
        this.chargingTimeHours = chargingTimeHours;
    }

    public Double getModelLkmHwy() {
        return modelLkmHwy;
    }

    public void setModelLkmHwy(Double modelLkmHwy) {
        this.modelLkmHwy = modelLkmHwy;
    }

    public Integer getModelEngineHorsePower() {
        return modelEngineHorsePower;
    }

    public void setModelEngineHorsePower(Integer modelEngineHorsePower) {
        this.modelEngineHorsePower = modelEngineHorsePower;
    }

    public String getModelTrim() {
        return modelTrim;
    }

    public void setModelTrim(String modelTrim) {
        this.modelTrim = modelTrim;
    }

    public String getMakeDisplay() {
        return makeDisplay;
    }

    public void setMakeDisplay(String makeDisplay) {
        this.makeDisplay = makeDisplay;
    }

    public Integer getModelEngineCc() {
        return modelEngineCc;
    }

    public void setModelEngineCc(Integer modelEngineCc) {
        this.modelEngineCc = modelEngineCc;
    }

    public Integer getModelEnginePowerRpm() {
        return modelEnginePowerRpm;
    }

    public void setModelEnginePowerRpm(Integer modelEnginePowerRpm) {
        this.modelEnginePowerRpm = modelEnginePowerRpm;
    }

    public String getModelBody() {
        return modelBody;
    }

    public void setModelBody(String modelBody) {
        this.modelBody = modelBody;
    }

    public String getModelDrive() {
        return modelDrive;
    }

    public void setModelDrive(String modelDrive) {
        this.modelDrive = modelDrive;
    }

    public Double getModelTopSpeedKph() {
        return modelTopSpeedKph;
    }

    public void setModelTopSpeedKph(Double modelTopSpeedKph) {
        this.modelTopSpeedKph = modelTopSpeedKph;
    }

    public Integer getModelDoors() {
        return modelDoors;
    }

    public void setModelDoors(Integer modelDoors) {
        this.modelDoors = modelDoors;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Double getModelCo2() {
        return modelCo2;
    }

    public void setModelCo2(Double modelCo2) {
        this.modelCo2 = modelCo2;
    }

    public Double getModelLkmMixed() {
        return modelLkmMixed;
    }

    public void setModelLkmMixed(Double modelLkmMixed) {
        this.modelLkmMixed = modelLkmMixed;
    }

    public String getModelEngineType() {
        return modelEngineType;
    }

    public void setModelEngineType(String modelEngineType) {
        this.modelEngineType = modelEngineType;
    }

    public String getMakeCountry() {
        return makeCountry;
    }

    public void setMakeCountry(String makeCountry) {
        this.makeCountry = makeCountry;
    }

    public Double getModelEngineTorqueRpm() {
        return modelEngineTorqueRpm;
    }

    public void setModelEngineTorqueRpm(Double modelEngineTorqueRpm) {
        this.modelEngineTorqueRpm = modelEngineTorqueRpm;
    }

    public String getModelEngineFuel() {
        return modelEngineFuel;
    }

    public void setModelEngineFuel(String modelEngineFuel) {
        this.modelEngineFuel = modelEngineFuel;
    }

    public Integer getModelEngineTorqueNm() {
        return modelEngineTorqueNm;
    }

    public void setModelEngineTorqueNm(Integer modelEngineTorqueNm) {
        this.modelEngineTorqueNm = modelEngineTorqueNm;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public void setModelYear(Integer modelYear) {
        this.modelYear = modelYear;
    }

    public Double getModel0To100Kph() {
        return model0To100Kph;
    }

    public void setModel0To100Kph(Double model0To100Kph) {
        this.model0To100Kph = model0To100Kph;
    }

    public String getModelEnginePosition() {
        return modelEnginePosition;
    }

    public void setModelEnginePosition(String modelEnginePosition) {
        this.modelEnginePosition = modelEnginePosition;
    }

    public String getModelTransmissionType() {
        return modelTransmissionType;
    }

    public void setModelTransmissionType(String modelTransmissionType) {
        this.modelTransmissionType = modelTransmissionType;
    }

    public Double getModelFuelCapL() {
        return modelFuelCapL;
    }

    public void setModelFuelCapL(Double modelFuelCapL) {
        this.modelFuelCapL = modelFuelCapL;
    }

    public Integer getModelEngineValvesPerCyl() {
        return modelEngineValvesPerCyl;
    }

    public void setModelEngineValvesPerCyl(Integer modelEngineValvesPerCyl) {
        this.modelEngineValvesPerCyl = modelEngineValvesPerCyl;
    }

    public Integer getModelEnginePowerPs() {
        return modelEnginePowerPs;
    }

    public void setModelEnginePowerPs(Integer modelEnginePowerPs) {
        this.modelEnginePowerPs = modelEnginePowerPs;
    }

    public Double getModelWeightKg() {
        return modelWeightKg;
    }

    public void setModelWeightKg(Double modelWeightKg) {
        this.modelWeightKg = modelWeightKg;
    }

    public String getModelEngineCyl() {
        return modelEngineCyl;
    }

    public void setModelEngineCyl(String modelEngineCyl) {
        this.modelEngineCyl = modelEngineCyl;
    }

    public Integer getModelSeats() {
        return modelSeats;
    }

    public void setModelSeats(Integer modelSeats) {
        this.modelSeats = modelSeats;
    }

    public Double getModelLkmCity() {
        return modelLkmCity;
    }

    public void setModelLkmCity(Double modelLkmCity) {
        this.modelLkmCity = modelLkmCity;
    }

    public String getModelEngineCompression() {
        return modelEngineCompression;
    }

    public void setModelEngineCompression(String modelEngineCompression) {
        this.modelEngineCompression = modelEngineCompression;
    }
}
