package com.example.app.data.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "car_orders_specs_data")
public class CarOrderSpecData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model_trim")
    private String modelTrim;

    @Column(name = "make_display")
    private String makeDisplay;

    @Column(name = "model_engine_cc")
    private Integer modelEngineCc;

    @Column(name = "model_engine_power_rpm")
    private Integer modelEnginePowerRpm;

    @Column(name = "model_body")
    private String modelBody;

    @Column(name = "model_drive")
    private String modelDrive;

    @Column(name = "model_top_speed_kph")
    private Integer modelTopSpeedKph;

    @Column(name = "model_doors")
    private Integer modelDoors;

    @Column(name = "model_name")
    private String modelName;

    @Column(name = "model_co2")
    private Double modelCo2;

    @Column(name = "model_lkm_mixed")
    private Double modelLkmMixed;

    @Column(name = "model_lkm_highway")
    private Double modelLkmHwy;

    @Column(name = "model_engine_type")
    private String modelEngineType;

    @Column(name = "make_country")
    private String makeCountry;

    @Column(name = "model_engine_torque_rpm")
    private Double modelEngineTorqueRpm;

    @Column(name = "model_engine_fuel")
    private String modelEngineFuel;

    @Column(name = "model_engine_torque_nm")
    private Integer modelEngineTorqueNm;

    @Column(name = "model_year")
    private Integer modelYear;

    @Column(name = "model_0_to_100_kph")
    private Double model0To100Kph;

    @Column(name = "model_engine_position")
    private String modelEnginePosition;

    @Column(name = "model_transmission_type")
    private String modelTransmissionType;

    @Column(name = "model_fuel_cap_l")
    private Double modelFuelCapL;

    @Column(name = "model_engine_valves_per_cyl")
    private Integer modelEngineValvesPerCyl;

    @Column(name = "model_engine_power_ps")
    private Integer modelEnginePowerPs;

    @Column(name = "model_weight_kg")
    private Double modelWeightKg;

    @Column(name = "model_engine_cyl")
    private String modelEngineCyl;

    @Column(name = "model_seats")
    private Integer modelSeats;

    @Column(name = "model_lkm_city")
    private Integer modelLkmCity;

    @Column(name = "model_engine_compression")
    private String modelEngineCompression;

    @Column(name = "model_horsepower")
    private Integer modelEngineHorsePower;

    @Column(name = "battery_capacity_kwh")
    private Double batteryCapacityKwh;

    @Column(name = "range_km")
    private Double rangeKm;

    @Column(name = "wheelbase_mm")
    private Integer wheelbaseMm;

    @Column(name = "length_mm")
    private Integer lengthMm;

    @Column(name = "width_mm")
    private Integer widthMm;

    @Column(name = "height_mm")
    private Integer heightMm;

    @Column(name = "cargo_capacity_l")
    private Double cargoCapacityL;

    @Column(name = "ground_clearance_mm")
    private Integer groundClearanceMm;

    @Column(name = "max_charging_power_kw")
    private Integer maxChargingPowerKw;

    @Column(name = "charging_time_hours")
    private Double chargingTimeHours;

    public CarOrderSpecData() {
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

    public void setModelFuelCapL(Double modelFuelCapL) {
        this.modelFuelCapL = modelFuelCapL;
    }

    public Integer getModelEngineHorsePower() {
        return modelEngineHorsePower;
    }

    public void setModelEngineHorsePower(Integer modelEngineHorsePower) {
        this.modelEngineHorsePower = modelEngineHorsePower;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getModelTopSpeedKph() {
        return modelTopSpeedKph;
    }

    public void setModelTopSpeedKph(Integer modelTopSpeedKph) {
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

    public void setModelFuelCapL(double modelFuelCapL) {
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

    public Integer getModelLkmCity() {
        return modelLkmCity;
    }

    public void setModelLkmCity(Integer modelLkmCity) {
        this.modelLkmCity = modelLkmCity;
    }

    public String getModelEngineCompression() {
        return modelEngineCompression;
    }

    public void setModelEngineCompression(String modelEngineCompression) {
        this.modelEngineCompression = modelEngineCompression;
    }
}
