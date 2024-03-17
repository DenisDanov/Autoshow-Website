package com.example.app.services.impl;

import com.example.app.data.DTOs.CarOrderSpecDataDTO;
import com.example.app.data.entities.CarOrderSpecData;
import com.example.app.data.repositories.CarOrderSpecDataRepository;
import com.example.app.services.CarOrderSpecDataService;
import com.example.app.services.ProxyControllerCarMenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarOrderSpecDataServiceImpl implements CarOrderSpecDataService {

    private final CarOrderSpecDataRepository carOrderSpecDataRepository;

    private final ProxyControllerCarMenuService proxyControllerCarMenuService;

    private final ModelMapper modelMapper;

    public CarOrderSpecDataServiceImpl(CarOrderSpecDataRepository carOrderSpecDataRepository, ProxyControllerCarMenuService proxyControllerCarMenuService, ModelMapper modelMapper) {
        this.carOrderSpecDataRepository = carOrderSpecDataRepository;
        this.proxyControllerCarMenuService = proxyControllerCarMenuService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<CarOrderSpecData> findCarOrderSpecDataByMakeDisplayAndModelNameAndModelYear(String make, String model, int year) {
        return this.carOrderSpecDataRepository.
                findCarOrderSpecDataByMakeDisplayAndModelNameAndModelYear(make, model, year);
    }

    @Override
    public void seedDatabase() {
        if (carOrderSpecDataRepository.findCarOrderSpecDataByMakeDisplayAndModelNameAndModelYear("Lamborghini", "Aventador", 2019).isEmpty()) {
            mapDTOtoDbObject(proxyControllerCarMenuService.proxyCarTrims("Lamborghini", "Aventador", 2019), "Lamborghini-Aventador-2019");
        }
        if (carOrderSpecDataRepository.findCarOrderSpecDataByMakeDisplayAndModelNameAndModelYear("Lamborghini", "Urus", 2022).isEmpty()) {
            mapDTOtoDbObject(new JSONObject(), "Lamborghini-Urus-2022");
        }
        if (carOrderSpecDataRepository.findCarOrderSpecDataByMakeDisplayAndModelNameAndModelYear("Porsche", "911 Carrera", 2015).isEmpty()) {
            mapDTOtoDbObject(new JSONObject(), "Porsche-911 Carrera-2015");
        }
        if (carOrderSpecDataRepository.findCarOrderSpecDataByMakeDisplayAndModelNameAndModelYear("Lamborghini", "Aventador", 2020).isEmpty()) {
            mapDTOtoDbObject(proxyControllerCarMenuService.proxyCarTrims("Lamborghini", "Aventador", 2020), "Lamborghini-Aventador-2020");
        }
        if (carOrderSpecDataRepository.findCarOrderSpecDataByMakeDisplayAndModelNameAndModelYear("Lamborghini", "Gallardo", 2007).isEmpty()) {
            mapDTOtoDbObject(proxyControllerCarMenuService.proxyCarTrims("Lamborghini", "Gallardo", 2007), "Lamborghini-Gallardo-2007");
        }
        if (carOrderSpecDataRepository.findCarOrderSpecDataByMakeDisplayAndModelNameAndModelYear("Toyota", "GR Supra", 2020).isEmpty()) {
            mapDTOtoDbObject(new JSONObject(), "Toyota-GR Supra-2020");
        }
        if (carOrderSpecDataRepository.findCarOrderSpecDataByMakeDisplayAndModelNameAndModelYear("Porsche", "718 Boxster S", 2016).isEmpty()) {
            mapDTOtoDbObject(new JSONObject(), "Porsche-718 Boxster S-2016");
        }
        if (carOrderSpecDataRepository.findCarOrderSpecDataByMakeDisplayAndModelNameAndModelYear("BMW", "X5 M Competition", 2022).isEmpty()) {
            mapDTOtoDbObject(new JSONObject(), "BMW-X5 M Competition-2022");
        }
        if (carOrderSpecDataRepository.findCarOrderSpecDataByMakeDisplayAndModelNameAndModelYear("McLaren", "P1", 2015).isEmpty()) {
            mapDTOtoDbObject(new JSONObject(), "McLaren-P1-2015");
        }
        if (carOrderSpecDataRepository.findCarOrderSpecDataByMakeDisplayAndModelNameAndModelYear("Tesla", "Model 3", 2023).isEmpty()) {
            mapDTOtoDbObject(new JSONObject(), "Tesla-Model 3-2023");
        }
    }

    private void mapDTOtoDbObject(JSONObject proxyControllerCarMenuService, String carName) {
        fulfillObject(carName, proxyControllerCarMenuService);
        ObjectMapper mapper = new ObjectMapper();
        try {
            CarOrderSpecDataDTO dto = mapper.readValue(proxyControllerCarMenuService.toString(), CarOrderSpecDataDTO.class);
            carOrderSpecDataRepository.saveAndFlush(modelMapper.map(dto, CarOrderSpecData.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JSONObject fulfillObject(String carName, JSONObject jsonObject) {
        switch (carName) {
            case "Lamborghini-Urus-2022", "Porsche-911 Carrera-2015", "Toyota-GR Supra-2020",
                    "Porsche-718 Boxster S-2016", "BMW-X5 M Competition-2022", "McLaren-P1-2015",
                    "Tesla-Model 3-2023":
                makeWholeObject(jsonObject, carName);
                break;
            case "Lamborghini-Aventador-2019", "Lamborghini-Aventador-2020":
                for (String key : jsonObject.keySet()) {
                    if (jsonObject.isNull(key)) {
                        jsonObject.put(key, changeNullValues(key, carName));
                    }
                }
                jsonObject.put("cargo_capacity_l", 140.0);
                jsonObject.put("ground_clearance_mm", 125);
                if (carName.equals("Lamborghini-Aventador-2019")) {
                    jsonObject.put("model_horsepower", 721);
                } else {
                    jsonObject.put("model_horsepower", 730);
                }
                break;
            case "Lamborghini-Gallardo-2007":
                for (String key : jsonObject.keySet()) {
                    if (jsonObject.isNull(key) || jsonObject.get(key).toString().isEmpty()) {
                        jsonObject.put(key, changeNullValues(key, carName));
                    }
                }
                jsonObject.put("cargo_capacity_l", 110.0);
                jsonObject.put("ground_clearance_mm", 107);
                jsonObject.put("model_horsepower", 512);
                break;
            default:
                break;
        }
        return jsonObject;
    }

    private void makeWholeObject(JSONObject jsonObject, String carName) {
        switch (carName) {
            case "Lamborghini-Urus-2022":
                jsonObject.put("model_trim", "Urus 4dr SUV AWD (4.0L 8cyl Turbo 8A)");
                jsonObject.put("make_display", "Lamborghini");
                jsonObject.put("model_engine_cc", 3996);
                jsonObject.put("model_engine_power_rpm", 6000);
                jsonObject.put("model_body", "SUV");
                jsonObject.put("model_drive", "All Wheel Drive");
                jsonObject.put("model_top_speed_kph", 305.0);
                jsonObject.put("model_doors", 4);
                jsonObject.put("model_name", "Urus");
                jsonObject.put("model_co2", 330.0);
                jsonObject.put("model_lkm_mixed", 12.7);
                jsonObject.put("model_engine_type", "V");
                jsonObject.put("make_country", "Italy");
                jsonObject.put("model_engine_torque_rpm", 2250.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 850);
                jsonObject.put("model_year", 2022);
                jsonObject.put("model_0_to_100_kph", 3.6);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Automatic");
                jsonObject.put("model_fuel_cap_l", 75.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 650);
                jsonObject.put("model_weight_kg", 2200.0);
                jsonObject.put("model_engine_cyl", "8");
                jsonObject.put("model_seats", 5);
                jsonObject.put("model_lkm_city", 15.0);
                jsonObject.put("model_lkm_hwy", 17.0);
                jsonObject.put("model_engine_compression", 9.0);
                jsonObject.put("model_horsepower", 641);
                jsonObject.put("model_wheelbase_mm", 3003); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 5112); // Overall length in millimeters
                jsonObject.put("model_width_mm", 2016); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1638); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 616); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 233); // Ground clearance in millimeters
                break;
            case "Porsche-911 Carrera-2015":
                jsonObject.put("model_trim", "911 Carrera 2dr Coupe (3.4L 6cyl 7AM)");
                jsonObject.put("make_display", "Porsche");
                jsonObject.put("model_engine_cc", 3436);
                jsonObject.put("model_engine_power_rpm", 7400);
                jsonObject.put("model_body", "Coupe");
                jsonObject.put("model_drive", "Rear Wheel Drive");
                jsonObject.put("model_top_speed_kph", 289.0);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "911 Carrera");
                jsonObject.put("model_co2", 186.0);
                jsonObject.put("model_lkm_mixed", 8.2);
                jsonObject.put("model_engine_type", "Flat");
                jsonObject.put("make_country", "Germany");
                jsonObject.put("model_engine_torque_rpm", 5600.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 390);
                jsonObject.put("model_year", 2015);
                jsonObject.put("model_0_to_100_kph", 4.8);
                jsonObject.put("model_engine_position", "Rear");
                jsonObject.put("model_transmission_type", "Automated Manual");
                jsonObject.put("model_fuel_cap_l", 64.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 350);
                jsonObject.put("model_weight_kg", 1415.0);
                jsonObject.put("model_engine_cyl", "6");
                jsonObject.put("model_seats", 4);
                jsonObject.put("model_lkm_city", 11.6);
                jsonObject.put("model_lkm_hwy", 13.6);
                jsonObject.put("model_engine_compression", 12.0);
                jsonObject.put("model_horsepower", 350);
                jsonObject.put("model_wheelbase_mm", 2450); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4491); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1808); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1303); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 145); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 120); // Ground clearance in millimeters
                break;
            case "Toyota-GR Supra-2020":
                jsonObject.put("model_trim", "3.0 Premium 2dr Coupe (3.0L 6cyl Turbo 8A)");
                jsonObject.put("make_display", "Toyota");
                jsonObject.put("model_engine_cc", 2998);
                jsonObject.put("model_engine_power_rpm", 5000);
                jsonObject.put("model_body", "Coupe");
                jsonObject.put("model_drive", "Rear Wheel Drive");
                jsonObject.put("model_top_speed_kph", 250.0);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "GR Supra");
                jsonObject.put("model_co2", 225.0);
                jsonObject.put("model_lkm_mixed", 7.7);
                jsonObject.put("model_engine_type", "Inline");
                jsonObject.put("make_country", "Japan");
                jsonObject.put("model_engine_torque_rpm", 1600.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 500);
                jsonObject.put("model_year", 2020);
                jsonObject.put("model_0_to_100_kph", 4.1);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Automatic");
                jsonObject.put("model_fuel_cap_l", 52.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 340);
                jsonObject.put("model_weight_kg", 1550.0);
                jsonObject.put("model_engine_cyl", "6");
                jsonObject.put("model_seats", 2);
                jsonObject.put("model_lkm_city", 10.0);
                jsonObject.put("model_lkm_hwy", 8.0);
                jsonObject.put("model_engine_compression", 10.2);
                jsonObject.put("model_horsepower", 335);
                jsonObject.put("model_wheelbase_mm", 2470); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4379); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1854); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1281); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 290); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 119); // Ground clearance in millimeters
                break;
            case "Porsche-718 Boxster S-2016":
                jsonObject.put("model_trim", "S 2dr Convertible (2.5L 4cyl Turbo 6M)");
                jsonObject.put("make_display", "Porsche");
                jsonObject.put("model_engine_cc", 2497);
                jsonObject.put("model_engine_power_rpm", 6500);
                jsonObject.put("model_body", "Convertible");
                jsonObject.put("model_drive", "Rear Wheel Drive");
                jsonObject.put("model_top_speed_kph", 285.0);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "718 Boxster S");
                jsonObject.put("model_co2", 184.0);
                jsonObject.put("model_lkm_mixed", 7.8);
                jsonObject.put("model_engine_type", "Flat");
                jsonObject.put("make_country", "Germany");
                jsonObject.put("model_engine_torque_rpm", 1900.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 420);
                jsonObject.put("model_year", 2016);
                jsonObject.put("model_0_to_100_kph", 4.4);
                jsonObject.put("model_engine_position", "Mid");
                jsonObject.put("model_transmission_type", "Manual");
                jsonObject.put("model_fuel_cap_l", 64.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 350);
                jsonObject.put("model_weight_kg", 1395.0);
                jsonObject.put("model_engine_cyl", "4");
                jsonObject.put("model_seats", 2);
                jsonObject.put("model_lkm_city", 9.0);
                jsonObject.put("model_lkm_hwy", 6.0);
                jsonObject.put("model_engine_compression", 9.5);
                jsonObject.put("model_horsepower", 345);
                jsonObject.put("model_wheelbase_mm", 2475); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4379); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1801); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1281); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 150); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 105); // Ground clearance in millimeters
                break;
            case "BMW-X5 M Competition-2022":
                jsonObject.put("model_trim", "X5 M Competition 4dr SUV AWD (4.4L 8cyl Turbo 8A)");
                jsonObject.put("make_display", "BMW");
                jsonObject.put("model_engine_cc", 4395);
                jsonObject.put("model_engine_power_rpm", 6000);
                jsonObject.put("model_body", "SUV");
                jsonObject.put("model_drive", "All Wheel Drive");
                jsonObject.put("model_top_speed_kph", 290.0);
                jsonObject.put("model_doors", 4);
                jsonObject.put("model_name", "X5 M");
                jsonObject.put("model_co2", 304.0);
                jsonObject.put("model_lkm_mixed", 12.3);
                jsonObject.put("model_engine_type", "V");
                jsonObject.put("make_country", "Germany");
                jsonObject.put("model_engine_torque_rpm", 1800.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 750);
                jsonObject.put("model_year", 2022);
                jsonObject.put("model_0_to_100_kph", 3.8);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Automatic");
                jsonObject.put("model_fuel_cap_l", 83.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 625);
                jsonObject.put("model_weight_kg", 2325.0);
                jsonObject.put("model_engine_cyl", "8");
                jsonObject.put("model_seats", 5);
                jsonObject.put("model_lkm_city", 14.0);
                jsonObject.put("model_lkm_hwy", 18.0);
                jsonObject.put("model_engine_compression", 10.0);
                jsonObject.put("model_horsepower", 617);
                jsonObject.put("model_wheelbase_mm", 2975); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4922); // Overall length in millimeters
                jsonObject.put("model_width_mm", 2004); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1747); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 650); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 211); // Ground clearance in millimeters
                break;
            case "McLaren-P1-2015":
                jsonObject.put("model_trim", "P1 2dr Coupe (3.8L Twin Turbo Gas/Electric Hybrid 7AM)");
                jsonObject.put("make_display", "McLaren");
                jsonObject.put("model_engine_cc", 3799);
                jsonObject.put("model_engine_power_rpm", 7500);
                jsonObject.put("model_body", "Coupe");
                jsonObject.put("model_drive", "Rear Wheel Drive");
                jsonObject.put("model_top_speed_kph", 350.0);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "P1");
                jsonObject.put("model_co2", 194.0);
                jsonObject.put("model_lkm_mixed", 9.0);
                jsonObject.put("model_engine_type", "V");
                jsonObject.put("make_country", "United Kingdom");
                jsonObject.put("model_engine_torque_rpm", 4000.0);
                jsonObject.put("model_engine_fuel", "Hybrid");
                jsonObject.put("model_engine_torque_nm", 900);
                jsonObject.put("model_year", 2015);
                jsonObject.put("model_0_to_100_kph", 2.8);
                jsonObject.put("model_engine_position", "Mid");
                jsonObject.put("model_transmission_type", "Automated Manual");
                jsonObject.put("model_fuel_cap_l", 72.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 916);
                jsonObject.put("model_weight_kg", 1490.0);
                jsonObject.put("model_engine_cyl", "8");
                jsonObject.put("model_seats", 2);
                jsonObject.put("model_lkm_city", 15.0);
                jsonObject.put("model_lkm_hwy", 17.0);
                jsonObject.put("model_engine_compression", 8.7);
                jsonObject.put("model_horsepower", 903);
                jsonObject.put("model_wheelbase_mm", 2670); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4588); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1946); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1138); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 425.0); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 120); // Ground clearance in millimeters
                break;
            case "Tesla-Model 3-2023":
                jsonObject.put("model_trim", "Standard Range Plus 4dr Sedan (electric DD)");
                jsonObject.put("make_display", "Tesla");
                jsonObject.put("model_top_speed_kph", 233.0); // Estimated top speed for Standard Range Plus model
                jsonObject.put("model_co2", 0.0); // Zero emissions for electric vehicles
                jsonObject.put("make_country", "United States");
                jsonObject.put("model_engine_torque_nm", 353); // Torque figure for Standard Range Plus model
                jsonObject.put("model_0_to_100_kph", 5.6); // Acceleration time for Standard Range Plus model
                jsonObject.put("model_engine_type", "Electric");
                jsonObject.put("model_transmission_type", "Single-Speed Automatic");
                jsonObject.put("model_engine_power_ps", 241); // Power output for Standard Range Plus model
                jsonObject.put("model_weight_kg", 1610.0); // Curb weight for Standard Range Plus model
                jsonObject.put("model_seats", 5); // Standard seating capacity
                jsonObject.put("model_horsepower", 241); // Horsepower equivalent for Standard Range Plus model
                // Additional specifications
                jsonObject.put("battery_capacity_kwh", 60.0); // Battery capacity in kilowatt-hours
                jsonObject.put("range_km", 420.0); // Estimated range in kilometers
                jsonObject.put("model_wheelbase_mm", 2875); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4694); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1849); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1443); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 425.0); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 140); // Ground clearance in millimeters
                jsonObject.put("max_charging_power_kw", 250); // Maximum charging power in kilowatts
                jsonObject.put("charging_time_hours", 4.5); // Estimated charging time from empty to full
                break;
            default:
                break;
        }
    }

    private String changeNullValues(String key, String carName) {

        switch (carName) {
            case "Lamborghini-Aventador-2019":
                return switch (key) {
                    case "model_engine_power_rpm" -> String.valueOf(8000);
                    case "model_engine_torque_rpm" -> String.valueOf(5500);
                    case "model_top_speed_kph" -> String.valueOf(350);
                    case "model_0_to_100_kph" -> String.valueOf(3.0);
                    case "model_seats" -> String.valueOf(2);
                    case "model_co2" -> String.valueOf(380.0);
                    case "model_wheelbase_mm" -> String.valueOf(2700);
                    case "model_width_mm" -> String.valueOf(2030);
                    case "model_height_mm" -> String.valueOf(1136);
                    case "model_length_mm" -> String.valueOf(4943);
                    default -> "";
                };
            case "Lamborghini-Aventador-2020":
                return switch (key) {
                    case "model_engine_power_rpm" -> String.valueOf(8500);
                    case "model_engine_torque_rpm" -> String.valueOf(6000);
                    case "model_top_speed_kph" -> String.valueOf(370);
                    case "model_0_to_100_kph" -> String.valueOf(2.9);
                    case "model_seats" -> String.valueOf(2);
                    case "model_co2" -> String.valueOf(370.0);
                    case "model_wheelbase_mm" -> String.valueOf(2700);
                    case "model_width_mm" -> String.valueOf(2030);
                    case "model_height_mm" -> String.valueOf(1136);
                    case "model_length_mm" -> String.valueOf(4943);
                    default -> "";
                };
            case "Lamborghini-Gallardo-2007":
                return switch (key) {
                    case "model_trim" -> "Gallardo Coupe 2dr Coupe AWD (5.0L 10cyl 6M)";
                    case "model_top_speed_kph" -> "309";
                    case "model_lkm_hwy" -> "17";
                    case "model_lkm_city" -> "10";
                    case "model_co2" -> "320.0";
                    default -> "";
                };
            default:

                return "";
        }
    }
}
