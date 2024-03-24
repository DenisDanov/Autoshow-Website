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
    public Optional<CarOrderSpecData> findCarOrderSpecDataByMakeDisplayAndModelNameAndModelYear(String text) {
        return this.carOrderSpecDataRepository.
                findCarOrderSpecDataByMakeDisplayAndModelNameAndModelYear(text);
    }

    @Override
    public void seedDatabase() {
        if (carOrderSpecDataRepository.count() == 0) {
            mapDTOtoDbObject(proxyControllerCarMenuService.proxyCarTrims("Lamborghini", "Aventador", 2019), "Lamborghini-Aventador-2019");
            mapDTOtoDbObject(new JSONObject(), "Lamborghini-Urus-2022");
            mapDTOtoDbObject(new JSONObject(), "Porsche-911 Carrera-2015");
            mapDTOtoDbObject(proxyControllerCarMenuService.proxyCarTrims("Lamborghini", "Aventador", 2020), "Lamborghini-Aventador-2020");
            mapDTOtoDbObject(proxyControllerCarMenuService.proxyCarTrims("Lamborghini", "Gallardo", 2007), "Lamborghini-Gallardo-2007");
            mapDTOtoDbObject(new JSONObject(), "Toyota-GR Supra-2020");
            mapDTOtoDbObject(new JSONObject(), "Porsche-Boxster-2016");
            mapDTOtoDbObject(new JSONObject(), "BMW-X5 M Competition-2022");
            mapDTOtoDbObject(new JSONObject(), "McLaren-P1-2015");
            mapDTOtoDbObject(new JSONObject(), "BMW-M4-2022");
            mapDTOtoDbObject(new JSONObject(), "Tesla-Model-3-2020");
            mapDTOtoDbObject(new JSONObject(), "BMW-M5-1999");
            mapDTOtoDbObject(new JSONObject(), "Bugatti-Chiron-2020");
            mapDTOtoDbObject(new JSONObject(), "Ferrari-F40-1992");
            mapDTOtoDbObject(new JSONObject(), "Ford-F-150-2022");
            mapDTOtoDbObject(new JSONObject(), "Jeep-Compass-2020");
            mapDTOtoDbObject(new JSONObject(), "Jeep-Grand Cherokee SRT-2017");
            mapDTOtoDbObject(new JSONObject(), "Lamborghini-Murcielago-2010");
            mapDTOtoDbObject(new JSONObject(), "McLaren-F1 GTR-1995");
            mapDTOtoDbObject(new JSONObject(), "McLaren-Senna-2020");
            mapDTOtoDbObject(new JSONObject(), "Mercedes-Benz-Brabus 800 S63-2022");
            mapDTOtoDbObject(new JSONObject(), "Mercedes-Benz-Brabus G900-2023");
            mapDTOtoDbObject(new JSONObject(), "Mercedes-Benz-E-Class-2014");
            mapDTOtoDbObject(new JSONObject(), "Mercedes-Benz-G-Class-2022");
            mapDTOtoDbObject(new JSONObject(), "Mercedes-Benz-Maybach GLS 600-2023");
            mapDTOtoDbObject(new JSONObject(), "Mercedes-Benz-S-Class-2022");
            mapDTOtoDbObject(new JSONObject(), "Mercedes-Benz-SL-Class-2022");
            mapDTOtoDbObject(new JSONObject(), "Mercedes-Benz-SLS AMG GT Final Edition-2020");
            mapDTOtoDbObject(new JSONObject(), "Nissan-GT-R R33-1995");
            mapDTOtoDbObject(new JSONObject(), "Nissan-GT-R-2017");
            mapDTOtoDbObject(new JSONObject(), "Nissan-Silvia-180SX-1996");
            mapDTOtoDbObject(new JSONObject(), "Porsche-918 Spyder-2015");
            mapDTOtoDbObject(new JSONObject(), "Porsche-GT3 RS-2023");
            mapDTOtoDbObject(new JSONObject(), "Rolls-Royce-Ghost-2022");
            mapDTOtoDbObject(new JSONObject(), "Subaru-Impreza-1998");
            mapDTOtoDbObject(new JSONObject(), "Volkswagen-Golf-2021");
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
                    "Porsche-Boxster-2016", "BMW-X5 M Competition-2022", "McLaren-P1-2015",
                    "Tesla-Model-3-2020", "BMW-M4-2022", "BMW-M5-1999", "Bugatti-Chiron-2020",
                    "Ferrari-F40-1992", "Ford-F-150-2022", "Jeep-Compass-2020",
                    "Jeep-Grand Cherokee SRT-2017", "Lamborghini-Murcielago-2010", "McLaren-F1 GTR-1995",
                    "McLaren-Senna-2020", "Mercedes-Benz-Brabus 800 S63-2022",
                    "Mercedes-Benz-Brabus G900-2023", "Mercedes-Benz-E-Class-2014",
                    "Mercedes-Benz-G-Class-2022", "Mercedes-Benz-Maybach GLS 600-2023",
                    "Mercedes-Benz-S-Class-2022", "Mercedes-Benz-SL-Class-2022",
                    "Mercedes-Benz-SLS AMG GT Final Edition-2020", "Nissan-GT-R R33-1995",
                    "Nissan-GT-R-2017", "Nissan-Silvia-180SX-1996", "Porsche-918 Spyder-2015",
                    "Porsche-GT3 RS-2023", "Rolls-Royce-Ghost-2022", "Subaru-Impreza-1998",
                    "Volkswagen-Golf-2021":
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
            case "Volkswagen-Golf-2021":
                jsonObject.put("model_trim", "Trendline 4dr Hatchback (1.4L 4cyl Turbo 6M)");
                jsonObject.put("make_display", "Volkswagen");
                jsonObject.put("model_engine_cc", 1395);
                jsonObject.put("model_engine_power_rpm", 5000);
                jsonObject.put("model_body", "Hatchback");
                jsonObject.put("model_drive", "Front Wheel Drive");
                jsonObject.put("model_top_speed_kph", 204.0);
                jsonObject.put("model_doors", 4);
                jsonObject.put("model_name", "Golf");
                jsonObject.put("model_co2", 118.0);
                jsonObject.put("model_lkm_mixed", 5.0);
                jsonObject.put("model_engine_type", "Inline");
                jsonObject.put("make_country", "Germany");
                jsonObject.put("model_engine_torque_rpm", 1400.0);
                jsonObject.put("model_engine_fuel", "Regular Unleaded");
                jsonObject.put("model_engine_torque_nm", 250);
                jsonObject.put("model_year", 2021);
                jsonObject.put("model_0_to_100_kph", 9.1);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Manual");
                jsonObject.put("model_fuel_cap_l", 50.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 130);
                jsonObject.put("model_weight_kg", 1235.0);
                jsonObject.put("model_engine_cyl", "4");
                jsonObject.put("model_seats", 5);
                jsonObject.put("model_lkm_city", 6.5);
                jsonObject.put("model_lkm_hwy", 4.5);
                jsonObject.put("model_engine_compression", 10.5);
                jsonObject.put("model_horsepower", 128);
                jsonObject.put("model_wheelbase_mm", 2630); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4284); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1789); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1492); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 381); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 150); // Ground clearance in millimeters
                break;
            case "Subaru-Impreza-1998":
                jsonObject.put("model_trim", "L AWD 4dr Sedan");
                jsonObject.put("make_display", "Subaru");
                jsonObject.put("model_engine_cc", 1994);
                jsonObject.put("model_engine_power_rpm", 5600);
                jsonObject.put("model_body", "Sedan");
                jsonObject.put("model_drive", "All Wheel Drive");
                jsonObject.put("model_top_speed_kph", 198.0);
                jsonObject.put("model_doors", 4);
                jsonObject.put("model_name", "Impreza");
                jsonObject.put("model_co2", 250);
                jsonObject.put("model_lkm_mixed", 8.4);
                jsonObject.put("model_engine_type", "Boxer");
                jsonObject.put("make_country", "Japan");
                jsonObject.put("model_engine_torque_rpm", 3600.0);
                jsonObject.put("model_engine_fuel", "Regular Unleaded");
                jsonObject.put("model_engine_torque_nm", 186);
                jsonObject.put("model_year", 1998);
                jsonObject.put("model_0_to_100_kph", 8.5);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Manual");
                jsonObject.put("model_fuel_cap_l", 50.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 125);
                jsonObject.put("model_weight_kg", 1235.0);
                jsonObject.put("model_engine_cyl", "4");
                jsonObject.put("model_seats", 5);
                jsonObject.put("model_lkm_city", 11.0);
                jsonObject.put("model_lkm_hwy", 7.4);
                jsonObject.put("model_engine_compression", 10.1);
                jsonObject.put("model_horsepower", 123);
                jsonObject.put("model_wheelbase_mm", 2520); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4360); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1690); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1400); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 384); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 150); // Ground clearance in millimeters
                break;
            case "Rolls-Royce-Ghost-2022":
                jsonObject.put("model_trim", "Base 4dr Sedan (6.8L 12cyl Turbo 8A)");
                jsonObject.put("make_display", "Rolls-Royce");
                jsonObject.put("model_engine_cc", 6749);
                jsonObject.put("model_engine_power_rpm", 5000);
                jsonObject.put("model_body", "Sedan");
                jsonObject.put("model_drive", "All Wheel Drive");
                jsonObject.put("model_top_speed_kph", 250.0);
                jsonObject.put("model_doors", 4);
                jsonObject.put("model_name", "Ghost");
                jsonObject.put("model_co2", 327.0);
                jsonObject.put("model_lkm_mixed", 15.0);
                jsonObject.put("model_engine_type", "V");
                jsonObject.put("make_country", "United Kingdom");
                jsonObject.put("model_engine_torque_rpm", 1700.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 900);
                jsonObject.put("model_year", 2022);
                jsonObject.put("model_0_to_100_kph", 4.8);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Automatic");
                jsonObject.put("model_fuel_cap_l", 82.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 571);
                jsonObject.put("model_weight_kg", 2360.0);
                jsonObject.put("model_engine_cyl", "12");
                jsonObject.put("model_seats", 5);
                jsonObject.put("model_lkm_city", 21.0);
                jsonObject.put("model_lkm_hwy", 11.0);
                jsonObject.put("model_engine_compression", 10.0);
                jsonObject.put("model_horsepower", 563);
                jsonObject.put("model_wheelbase_mm", 3295); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 5546); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1978); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1571); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 500); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 137); // Ground clearance in millimeters
                break;
            case "Porsche-GT3 RS-2023":
                jsonObject.put("model_trim", "GT3 RS 2dr Coupe (4.0L 6cyl 7AM)");
                jsonObject.put("make_display", "Porsche");
                jsonObject.put("model_engine_cc", 3996);
                jsonObject.put("model_engine_power_rpm", 8400);
                jsonObject.put("model_body", "Coupe");
                jsonObject.put("model_drive", "Rear Wheel Drive");
                jsonObject.put("model_top_speed_kph", 312.0);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "GT3 RS");
                jsonObject.put("model_co2", 293.0);
                jsonObject.put("model_lkm_mixed", 13.4);
                jsonObject.put("model_engine_type", "Flat");
                jsonObject.put("make_country", "Germany");
                jsonObject.put("model_engine_torque_rpm", 6900.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 470);
                jsonObject.put("model_year", 2023);
                jsonObject.put("model_0_to_100_kph", 3.2);
                jsonObject.put("model_engine_position", "Rear");
                jsonObject.put("model_transmission_type", "Automated Manual");
                jsonObject.put("model_fuel_cap_l", 68.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 520);
                jsonObject.put("model_weight_kg", 1415.0);
                jsonObject.put("model_engine_cyl", "6");
                jsonObject.put("model_seats", 2);
                jsonObject.put("model_lkm_city", 17.1);
                jsonObject.put("model_lkm_hwy", 9.8);
                jsonObject.put("model_engine_compression", 13.3);
                jsonObject.put("model_horsepower", 513);
                jsonObject.put("model_wheelbase_mm", 2457); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4602); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1880); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1311); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 125); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 109); // Ground clearance in millimeters
                break;
            case "Porsche-918 Spyder-2015":
                jsonObject.put("model_trim", "918 Spyder 2dr Convertible AWD (4.6L 8cyl 7AM)");
                jsonObject.put("make_display", "Porsche");
                jsonObject.put("model_engine_cc", 4593);
                jsonObject.put("model_engine_power_rpm", 8600);
                jsonObject.put("model_body", "Convertible");
                jsonObject.put("model_drive", "All Wheel Drive");
                jsonObject.put("model_top_speed_kph", 340.0);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "918 Spyder");
                jsonObject.put("model_co2", 210);
                jsonObject.put("model_lkm_mixed", 12.2);
                jsonObject.put("model_engine_type", "V");
                jsonObject.put("make_country", "Germany");
                jsonObject.put("model_engine_torque_rpm", 6000.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded");
                jsonObject.put("model_engine_torque_nm", 530);
                jsonObject.put("model_year", 2015);
                jsonObject.put("model_0_to_100_kph", 2.5);
                jsonObject.put("model_engine_position", "Middle");
                jsonObject.put("model_transmission_type", "Automated Manual");
                jsonObject.put("model_fuel_cap_l", 70.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 887);
                jsonObject.put("model_weight_kg", 1634.0);
                jsonObject.put("model_engine_cyl", "8");
                jsonObject.put("model_seats", 2);
                jsonObject.put("model_lkm_city", 13.5); // No data available
                jsonObject.put("model_lkm_hwy", 11.3); // No data available
                jsonObject.put("model_engine_compression", 12.0);
                jsonObject.put("model_horsepower", 875);
                jsonObject.put("model_wheelbase_mm", 2730); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4643); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1940); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1167); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 30); // No data available
                jsonObject.put("ground_clearance_mm", 85); // Ground clearance in millimeters
                break;
            case "Nissan-Silvia-180SX-1996":
                jsonObject.put("model_trim", "180SX 2dr Coupe (2.0L 4cyl Turbo 5M)");
                jsonObject.put("make_display", "Nissan");
                jsonObject.put("model_engine_cc", 1998);
                jsonObject.put("model_engine_power_rpm", 6400);
                jsonObject.put("model_body", "Coupe");
                jsonObject.put("model_drive", "Rear Wheel Drive");
                jsonObject.put("model_top_speed_kph", 210);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "Silvia-180SX");
                jsonObject.put("model_co2", 230);
                jsonObject.put("model_lkm_mixed", 13.7);
                jsonObject.put("model_engine_type", "Inline");
                jsonObject.put("make_country", "Japan");
                jsonObject.put("model_engine_torque_rpm", 4800.0);
                jsonObject.put("model_engine_fuel", "Regular Unleaded");
                jsonObject.put("model_engine_torque_nm", 235);
                jsonObject.put("model_year", 1996);
                jsonObject.put("model_0_to_100_kph", 5.4);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Manual");
                jsonObject.put("model_fuel_cap_l", 70.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 205);
                jsonObject.put("model_weight_kg", 1230.0);
                jsonObject.put("model_engine_cyl", "4");
                jsonObject.put("model_seats", 4);
                jsonObject.put("model_lkm_city", 17.3);
                jsonObject.put("model_lkm_hwy", 12.6);
                jsonObject.put("model_engine_compression", 8.5);
                jsonObject.put("model_horsepower", 462);
                jsonObject.put("model_wheelbase_mm", 2475); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4510); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1690); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1290); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 50);
                jsonObject.put("ground_clearance_mm", 120);
                break;
            case "Nissan-GT-R-2017":
                jsonObject.put("model_trim", "GT-R Nismo 2dr Coupe AWD (3.8L 6cyl Turbo 6AM)");
                jsonObject.put("make_display", "Nissan");
                jsonObject.put("model_engine_cc", 3799);
                jsonObject.put("model_engine_power_rpm", 6800);
                jsonObject.put("model_body", "Coupe");
                jsonObject.put("model_drive", "All Wheel Drive");
                jsonObject.put("model_top_speed_kph", 307.0);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "GT-R");
                jsonObject.put("model_co2", 330.0);
                jsonObject.put("model_lkm_mixed", 11.7);
                jsonObject.put("model_engine_type", "V");
                jsonObject.put("make_country", "Japan");
                jsonObject.put("model_engine_torque_rpm", 3200.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 637);
                jsonObject.put("model_year", 2017);
                jsonObject.put("model_0_to_100_kph", 2.8);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Automated Manual");
                jsonObject.put("model_fuel_cap_l", 74.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 570);
                jsonObject.put("model_weight_kg", 1752.0);
                jsonObject.put("model_engine_cyl", "6");
                jsonObject.put("model_seats", 4);
                jsonObject.put("model_lkm_city", 14.0);
                jsonObject.put("model_lkm_hwy", 10.0);
                jsonObject.put("model_engine_compression", 9.0);
                jsonObject.put("model_horsepower", 592);
                jsonObject.put("model_wheelbase_mm", 2780); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4670); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1895); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1370); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 140);
                jsonObject.put("ground_clearance_mm", 130);
                break;
            case "Nissan-GT-R R33-1995":
                jsonObject.put("model_trim", "GT-R R33 2dr Coupe AWD (2.6L 6cyl Turbo 5M)");
                jsonObject.put("make_display", "Nissan");
                jsonObject.put("model_engine_cc", 2568);
                jsonObject.put("model_engine_power_rpm", 6800);
                jsonObject.put("model_body", "Coupe");
                jsonObject.put("model_drive", "All Wheel Drive");
                jsonObject.put("model_top_speed_kph", 250.0);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "GT-R R33");
                jsonObject.put("model_co2", 230.0);
                jsonObject.put("model_lkm_mixed", 12.5);
                jsonObject.put("model_engine_type", "Inline");
                jsonObject.put("make_country", "Japan");
                jsonObject.put("model_engine_torque_rpm", 4400.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 392);
                jsonObject.put("model_year", 1995);
                jsonObject.put("model_0_to_100_kph", 5.0); // Approximate value
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Manual");
                jsonObject.put("model_fuel_cap_l", 60.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 280);
                jsonObject.put("model_weight_kg", 1540.0);
                jsonObject.put("model_engine_cyl", "6");
                jsonObject.put("model_seats", 4);
                jsonObject.put("model_lkm_city", 16.0); // Approximate value
                jsonObject.put("model_lkm_hwy", 10.0); // Approximate value
                jsonObject.put("model_engine_compression", 8.5); // Approximate value
                jsonObject.put("model_horsepower", 276);
                jsonObject.put("model_wheelbase_mm", 2720); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4675); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1780); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1360); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 200); // No data available
                jsonObject.put("ground_clearance_mm", 130); // No data available
                break;
            case "Mercedes-Benz-SLS AMG GT Final Edition-2020":
                jsonObject.put("model_trim", "SLS AMG GT Final Edition 2dr Coupe (6.2L V8 7A)");
                jsonObject.put("make_display", "Mercedes-Benz");
                jsonObject.put("model_engine_cc", 6208);
                jsonObject.put("model_engine_power_rpm", 6800);
                jsonObject.put("model_body", "Coupe");
                jsonObject.put("model_drive", "Rear Wheel Drive");
                jsonObject.put("model_top_speed_kph", 320.0);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "SLS AMG GT Final Edition");
                jsonObject.put("model_co2", 356.0);
                jsonObject.put("model_lkm_mixed", 13.7);
                jsonObject.put("model_engine_type", "V8");
                jsonObject.put("make_country", "Germany");
                jsonObject.put("model_engine_torque_rpm", 4750.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 651);
                jsonObject.put("model_year", 2020);
                jsonObject.put("model_0_to_100_kph", 3.6);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Automatic");
                jsonObject.put("model_fuel_cap_l", 85.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 591);
                jsonObject.put("model_weight_kg", 1620.0);
                jsonObject.put("model_engine_cyl", "V8");
                jsonObject.put("model_seats", 2);
                jsonObject.put("model_lkm_city", 17.0);
                jsonObject.put("model_lkm_hwy", 9.0);
                jsonObject.put("model_engine_compression", 11.3);
                jsonObject.put("model_horsepower", 583);
                jsonObject.put("model_wheelbase_mm", 2680); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4641); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1939); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1262); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 176); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 109); // Ground clearance in millimeters
                break;
            case "Mercedes-Benz-SL-Class-2022":
                jsonObject.put("model_trim", "SL 500 Roadster (4.4L V8 Turbo 9A)");
                jsonObject.put("make_display", "Mercedes-Benz");
                jsonObject.put("model_engine_cc", 3982);
                jsonObject.put("model_engine_power_rpm", 5500);
                jsonObject.put("model_body", "Roadster");
                jsonObject.put("model_drive", "Rear Wheel Drive");
                jsonObject.put("model_top_speed_kph", 250.0);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "SL-Class");
                jsonObject.put("model_co2", 219.0);
                jsonObject.put("model_lkm_mixed", 9.4);
                jsonObject.put("model_engine_type", "V8");
                jsonObject.put("make_country", "Germany");
                jsonObject.put("model_engine_torque_rpm", 2000.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 700);
                jsonObject.put("model_year", 2022);
                jsonObject.put("model_0_to_100_kph", 4.6);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Automatic");
                jsonObject.put("model_fuel_cap_l", 78.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 456);
                jsonObject.put("model_weight_kg", 1935.0);
                jsonObject.put("model_engine_cyl", "V8");
                jsonObject.put("model_seats", 2);
                jsonObject.put("model_lkm_city", 11.7);
                jsonObject.put("model_lkm_hwy", 7.9);
                jsonObject.put("model_engine_compression", 10.5);
                jsonObject.put("model_horsepower", 450);
                jsonObject.put("model_wheelbase_mm", 2870); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4707); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1887); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1301); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 295); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 122); // Ground clearance in millimeters
                break;
            case "Mercedes-Benz-S-Class-2022":
                jsonObject.put("model_trim", "S 580 4MATIC Sedan (4.0L V8 Turbo 9A)");
                jsonObject.put("make_display", "Mercedes-Benz");
                jsonObject.put("model_engine_cc", 3982);
                jsonObject.put("model_engine_power_rpm", 5500);
                jsonObject.put("model_body", "Sedan");
                jsonObject.put("model_drive", "All Wheel Drive");
                jsonObject.put("model_top_speed_kph", 250.0);
                jsonObject.put("model_doors", 4);
                jsonObject.put("model_name", "S-Class");
                jsonObject.put("model_co2", 229.0);
                jsonObject.put("model_lkm_mixed", 9.8);
                jsonObject.put("model_engine_type", "V8");
                jsonObject.put("make_country", "Germany");
                jsonObject.put("model_engine_torque_rpm", 2000.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 700);
                jsonObject.put("model_year", 2022);
                jsonObject.put("model_0_to_100_kph", 4.8);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Automatic");
                jsonObject.put("model_fuel_cap_l", 90.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 510);
                jsonObject.put("model_weight_kg", 2175.0);
                jsonObject.put("model_engine_cyl", "V8");
                jsonObject.put("model_seats", 5);
                jsonObject.put("model_lkm_city", 12.4);
                jsonObject.put("model_lkm_hwy", 8.3);
                jsonObject.put("model_engine_compression", 10.5);
                jsonObject.put("model_horsepower", 503);
                jsonObject.put("model_wheelbase_mm", 3125); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 5279); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1954); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1516); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 550); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 109); // Ground clearance in millimeters
                break;
            case "Mercedes-Benz-Maybach GLS 600-2023":
                jsonObject.put("model_trim", "GLS 600 4MATIC SUV (4.0L V8 Turbo + Elec 9A)");
                jsonObject.put("make_display", "Mercedes-Benz");
                jsonObject.put("model_engine_cc", 3982);
                jsonObject.put("model_engine_power_rpm", 5500);
                jsonObject.put("model_body", "SUV");
                jsonObject.put("model_drive", "All Wheel Drive");
                jsonObject.put("model_top_speed_kph", 250.0);
                jsonObject.put("model_doors", 5);
                jsonObject.put("model_name", "Maybach GLS 600");
                jsonObject.put("model_co2", 283.0);
                jsonObject.put("model_lkm_mixed", 11.7);
                jsonObject.put("model_engine_type", "V8");
                jsonObject.put("make_country", "Germany");
                jsonObject.put("model_engine_torque_rpm", 2000.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 730);
                jsonObject.put("model_year", 2023);
                jsonObject.put("model_0_to_100_kph", 4.9);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Automatic");
                jsonObject.put("model_fuel_cap_l", 92.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 558);
                jsonObject.put("model_weight_kg", 2734.0);
                jsonObject.put("model_engine_cyl", "V8");
                jsonObject.put("model_seats", 5);
                jsonObject.put("model_lkm_city", 14.7);
                jsonObject.put("model_lkm_hwy", 10.2);
                jsonObject.put("model_engine_compression", 10.5);
                jsonObject.put("model_horsepower", 550);
                jsonObject.put("model_wheelbase_mm", 3135); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 5371); // Overall length in millimeters
                jsonObject.put("model_width_mm", 2031); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1823); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 525); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 241); // Ground clearance in millimeters
                break;
            case "Mercedes-Benz-G-Class-2022":
                jsonObject.put("model_trim", "G 550 4MATIC SUV (4.0L V8 Turbo 9A)");
                jsonObject.put("make_display", "Mercedes-Benz");
                jsonObject.put("model_engine_cc", 3982);
                jsonObject.put("model_engine_power_rpm", 6000);
                jsonObject.put("model_body", "SUV");
                jsonObject.put("model_drive", "All Wheel Drive");
                jsonObject.put("model_top_speed_kph", 230.0);
                jsonObject.put("model_doors", 5);
                jsonObject.put("model_name", "G-Class");
                jsonObject.put("model_co2", 330.0);
                jsonObject.put("model_lkm_mixed", 13.8);
                jsonObject.put("model_engine_type", "V8");
                jsonObject.put("make_country", "Germany");
                jsonObject.put("model_engine_torque_rpm", 2500.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 770);
                jsonObject.put("model_year", 2022);
                jsonObject.put("model_0_to_100_kph", 5.8);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Automatic");
                jsonObject.put("model_fuel_cap_l", 100.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 422);
                jsonObject.put("model_weight_kg", 2465.0);
                jsonObject.put("model_engine_cyl", "V8");
                jsonObject.put("model_seats", 5);
                jsonObject.put("model_lkm_city", 15.7);
                jsonObject.put("model_lkm_hwy", 11.8);
                jsonObject.put("model_engine_compression", 10.5);
                jsonObject.put("model_horsepower", 416);
                jsonObject.put("model_wheelbase_mm", 2890); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4935); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1978); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1978); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 667); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 241); // Ground clearance in millimeters
                break;
            case "Mercedes-Benz-E-Class-2014":
                jsonObject.put("model_trim", "E 350 4dr Sedan (3.5L V6 7A)");
                jsonObject.put("make_display", "Mercedes-Benz");
                jsonObject.put("model_engine_cc", 3498);
                jsonObject.put("model_engine_power_rpm", 6500);
                jsonObject.put("model_body", "Sedan");
                jsonObject.put("model_drive", "Rear Wheel Drive");
                jsonObject.put("model_top_speed_kph", 250.0);
                jsonObject.put("model_doors", 4);
                jsonObject.put("model_name", "E-Class");
                jsonObject.put("model_co2", 178.0);
                jsonObject.put("model_lkm_mixed", 9.8);
                jsonObject.put("model_engine_type", "V6");
                jsonObject.put("make_country", "Germany");
                jsonObject.put("model_engine_torque_rpm", 3500.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded");
                jsonObject.put("model_engine_torque_nm", 350);
                jsonObject.put("model_year", 2014);
                jsonObject.put("model_0_to_100_kph", 6.5);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Automatic");
                jsonObject.put("model_fuel_cap_l", 59.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 302);
                jsonObject.put("model_weight_kg", 1785.0);
                jsonObject.put("model_engine_cyl", "V6");
                jsonObject.put("model_seats", 5);
                jsonObject.put("model_lkm_city", 12.4);
                jsonObject.put("model_lkm_hwy", 8.1);
                jsonObject.put("model_engine_compression", 12.2);
                jsonObject.put("model_horsepower", 302);
                jsonObject.put("model_wheelbase_mm", 2874); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4879); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1854); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1476); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 540); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 123); // Ground clearance in millimeters
                break;
            case "Mercedes-Benz-Brabus G900-2023":
                jsonObject.put("model_trim", "Brabus G900 4dr SUV AWD (4.0L V8 Turbo 9A)");
                jsonObject.put("make_display", "Mercedes-Benz");
                jsonObject.put("model_engine_cc", 3982);
                jsonObject.put("model_engine_power_rpm", 6500);
                jsonObject.put("model_body", "SUV");
                jsonObject.put("model_drive", "All Wheel Drive");
                jsonObject.put("model_top_speed_kph", 280.0);
                jsonObject.put("model_doors", 4);
                jsonObject.put("model_name", "Brabus G900");
                jsonObject.put("model_co2", 280.0);
                jsonObject.put("model_lkm_mixed", 13.5);
                jsonObject.put("model_engine_type", "V8");
                jsonObject.put("make_country", "Germany");
                jsonObject.put("model_engine_torque_rpm", 3500.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded");
                jsonObject.put("model_engine_torque_nm", 1050);
                jsonObject.put("model_year", 2023);
                jsonObject.put("model_0_to_100_kph", 3.8);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Automatic");
                jsonObject.put("model_fuel_cap_l", 85.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 900);
                jsonObject.put("model_weight_kg", 2500.0);
                jsonObject.put("model_engine_cyl", "V8");
                jsonObject.put("model_seats", 5);
                jsonObject.put("model_lkm_city", 17.0);
                jsonObject.put("model_lkm_hwy", 11.0);
                jsonObject.put("model_engine_compression", 9.5);
                jsonObject.put("model_horsepower", 900);
                jsonObject.put("model_wheelbase_mm", 3139); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 5235); // Overall length in millimeters
                jsonObject.put("model_width_mm", 2026); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1776); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 700); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 220); // Ground clearance in millimeters
                break;
            case "Mercedes-Benz-Brabus 800 S63-2022":
                jsonObject.put("model_trim", "Brabus 800 S63 4dr Sedan (4.0L V8 Turbo 9A)");
                jsonObject.put("make_display", "Mercedes-Benz");
                jsonObject.put("model_engine_cc", 3982);
                jsonObject.put("model_engine_power_rpm", 6000);
                jsonObject.put("model_body", "Sedan");
                jsonObject.put("model_drive", "All Wheel Drive");
                jsonObject.put("model_top_speed_kph", 280.0);
                jsonObject.put("model_doors", 4);
                jsonObject.put("model_name", "Brabus 800 S63");
                jsonObject.put("model_co2", 260.0);
                jsonObject.put("model_lkm_mixed", 11.3);
                jsonObject.put("model_engine_type", "V8");
                jsonObject.put("make_country", "Germany");
                jsonObject.put("model_engine_torque_rpm", 3000.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded");
                jsonObject.put("model_engine_torque_nm", 1000);
                jsonObject.put("model_year", 2022);
                jsonObject.put("model_0_to_100_kph", 3.4);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Automatic");
                jsonObject.put("model_fuel_cap_l", 80.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 800);
                jsonObject.put("model_weight_kg", 2180.0);
                jsonObject.put("model_engine_cyl", "V8");
                jsonObject.put("model_seats", 5);
                jsonObject.put("model_lkm_city", 15.0);
                jsonObject.put("model_lkm_hwy", 9.0);
                jsonObject.put("model_engine_compression", 8.6);
                jsonObject.put("model_horsepower", 800);
                jsonObject.put("model_wheelbase_mm", 3216); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 5107); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1953); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1459); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 530); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 100); // Ground clearance in millimeters
                break;
            case "McLaren-Senna-2020":
                jsonObject.put("model_trim", "Senna 2dr Coupe (4.0L 8cyl Turbo 7AM)");
                jsonObject.put("make_display", "McLaren");
                jsonObject.put("model_engine_cc", 3994);
                jsonObject.put("model_engine_power_rpm", 7250);
                jsonObject.put("model_body", "Coupe");
                jsonObject.put("model_drive", "Rear Wheel Drive");
                jsonObject.put("model_top_speed_kph", 340.0);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "Senna");
                jsonObject.put("model_co2", 280.0);
                jsonObject.put("model_lkm_mixed", 11.2);
                jsonObject.put("model_engine_type", "V");
                jsonObject.put("make_country", "United Kingdom");
                jsonObject.put("model_engine_torque_rpm", 5900.0);
                jsonObject.put("model_engine_fuel", "Gasoline");
                jsonObject.put("model_engine_torque_nm", 800);
                jsonObject.put("model_year", 2020);
                jsonObject.put("model_0_to_100_kph", 2.8);
                jsonObject.put("model_engine_position", "Mid");
                jsonObject.put("model_transmission_type", "Automated Manual");
                jsonObject.put("model_fuel_cap_l", 72.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 800);
                jsonObject.put("model_weight_kg", 1198.0);
                jsonObject.put("model_engine_cyl", "8");
                jsonObject.put("model_seats", 2);
                jsonObject.put("model_lkm_city", 0.0);
                jsonObject.put("model_lkm_hwy", 0.0);
                jsonObject.put("model_engine_compression", "9.3:1");
                jsonObject.put("model_horsepower", 800);
                jsonObject.put("model_wheelbase_mm", 2675); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4694); // Overall length in millimeters
                jsonObject.put("model_width_mm", 2088); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1198); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 0); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 120); // Ground clearance in millimeters
                break;
            case "McLaren-F1 GTR-1995":
                jsonObject.put("model_trim", "F1 GTR 2dr Coupe (6.1L 12cyl 6M)");
                jsonObject.put("make_display", "McLaren");
                jsonObject.put("model_engine_cc", 6064);
                jsonObject.put("model_engine_power_rpm", 7500);
                jsonObject.put("model_body", "Coupe");
                jsonObject.put("model_drive", "Rear Wheel Drive");
                jsonObject.put("model_top_speed_kph", 372.0);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "F1 GTR");
                jsonObject.put("model_co2", 0.0);
                jsonObject.put("model_lkm_mixed", 0.0);
                jsonObject.put("model_engine_type", "V");
                jsonObject.put("make_country", "United Kingdom");
                jsonObject.put("model_engine_torque_rpm", 5600.0);
                jsonObject.put("model_engine_fuel", "Gasoline");
                jsonObject.put("model_engine_torque_nm", 705);
                jsonObject.put("model_year", 1995);
                jsonObject.put("model_0_to_100_kph", 3.1);
                jsonObject.put("model_engine_position", "Mid");
                jsonObject.put("model_transmission_type", "Manual");
                jsonObject.put("model_fuel_cap_l", 90.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 627);
                jsonObject.put("model_weight_kg", 1050.0);
                jsonObject.put("model_engine_cyl", "12");
                jsonObject.put("model_seats", 2);
                jsonObject.put("model_lkm_city", 0.0);
                jsonObject.put("model_lkm_hwy", 0.0);
                jsonObject.put("model_engine_compression", "11.0:1");
                jsonObject.put("model_horsepower", 627);
                jsonObject.put("model_wheelbase_mm", 2718); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4689); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1820); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1140); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 0); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 130); // Ground clearance in millimeters
                break;
            case "Lamborghini-Murcielago-2010":
                jsonObject.put("model_trim", "Murcielago LP 670-4 SuperVeloce 2dr Coupe AWD (6.5L 12cyl 6M)");
                jsonObject.put("make_display", "Lamborghini");
                jsonObject.put("model_engine_cc", 6496);
                jsonObject.put("model_engine_power_rpm", 8000);
                jsonObject.put("model_body", "Coupe");
                jsonObject.put("model_drive", "All Wheel Drive");
                jsonObject.put("model_top_speed_kph", 342.0);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "Murcielago");
                jsonObject.put("model_co2", 490.0);
                jsonObject.put("model_lkm_mixed", 29.4);
                jsonObject.put("model_engine_type", "V");
                jsonObject.put("make_country", "Italy");
                jsonObject.put("model_engine_torque_rpm", 6600.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 660);
                jsonObject.put("model_year", 2010);
                jsonObject.put("model_0_to_100_kph", 3.2);
                jsonObject.put("model_engine_position", "Mid");
                jsonObject.put("model_transmission_type", "Manual");
                jsonObject.put("model_fuel_cap_l", 100.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 670);
                jsonObject.put("model_weight_kg", 1565.0);
                jsonObject.put("model_engine_cyl", "12");
                jsonObject.put("model_seats", 2);
                jsonObject.put("model_lkm_city", 24.4);
                jsonObject.put("model_lkm_hwy", 18.2);
                jsonObject.put("model_engine_compression", "11.0:1");
                jsonObject.put("model_horsepower", 661);
                jsonObject.put("model_wheelbase_mm", 2665); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4610); // Overall length in millimeters
                jsonObject.put("model_width_mm", 2058); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1130); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 140); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 110); // Ground clearance in millimeters
                break;
            case "Jeep-Grand Cherokee SRT-2017":
                jsonObject.put("model_trim", "Grand Cherokee SRT 4dr SUV 4WD (6.4L 8cyl 8A)");
                jsonObject.put("make_display", "Jeep");
                jsonObject.put("model_engine_cc", 6417);
                jsonObject.put("model_engine_power_rpm", 6250);
                jsonObject.put("model_body", "SUV");
                jsonObject.put("model_drive", "Four Wheel Drive");
                jsonObject.put("model_top_speed_kph", 257.0);
                jsonObject.put("model_doors", 4);
                jsonObject.put("model_name", "Grand Cherokee SRT");
                jsonObject.put("model_co2", 338.0);
                jsonObject.put("model_lkm_mixed", 18.3);
                jsonObject.put("model_engine_type", "V");
                jsonObject.put("make_country", "United States");
                jsonObject.put("model_engine_torque_rpm", 4100.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 637);
                jsonObject.put("model_year", 2017);
                jsonObject.put("model_0_to_100_kph", 4.4);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Automatic");
                jsonObject.put("model_fuel_cap_l", 93.0);
                jsonObject.put("model_engine_valves_per_cyl", 2);
                jsonObject.put("model_engine_power_ps", 468);
                jsonObject.put("model_weight_kg", 2293.0);
                jsonObject.put("model_engine_cyl", "8");
                jsonObject.put("model_seats", 5);
                jsonObject.put("model_lkm_city", 16.8);
                jsonObject.put("model_lkm_hwy", 11.2);
                jsonObject.put("model_engine_compression", 10.9);
                jsonObject.put("model_horsepower", 475);
                jsonObject.put("model_wheelbase_mm", 2915); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4828); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1954); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1771); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 1027); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 204); // Ground clearance in millimeters
                break;
            case "Jeep-Compass-2020":
                jsonObject.put("model_trim", "Compass Sport 4dr SUV (2.4L 4cyl 6M)");
                jsonObject.put("make_display", "Jeep");
                jsonObject.put("model_engine_cc", 2360);
                jsonObject.put("model_engine_power_rpm", 6400);
                jsonObject.put("model_body", "SUV");
                jsonObject.put("model_drive", "Front Wheel Drive");
                jsonObject.put("model_top_speed_kph", 193.0);
                jsonObject.put("model_doors", 4);
                jsonObject.put("model_name", "Compass");
                jsonObject.put("model_co2", 208.0);
                jsonObject.put("model_lkm_mixed", 9.8);
                jsonObject.put("model_engine_type", "Inline");
                jsonObject.put("make_country", "United States");
                jsonObject.put("model_engine_torque_rpm", 4400.0);
                jsonObject.put("model_engine_fuel", "Regular Unleaded");
                jsonObject.put("model_engine_torque_nm", 229);
                jsonObject.put("model_year", 2020);
                jsonObject.put("model_0_to_100_kph", 9.0);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Manual");
                jsonObject.put("model_fuel_cap_l", 51.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 180);
                jsonObject.put("model_weight_kg", 1492.0);
                jsonObject.put("model_engine_cyl", "4");
                jsonObject.put("model_seats", 5);
                jsonObject.put("model_lkm_city", 11.8);
                jsonObject.put("model_lkm_hwy", 7.6);
                jsonObject.put("model_engine_compression", 10.0);
                jsonObject.put("model_horsepower", 178);
                jsonObject.put("model_wheelbase_mm", 2636); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4394); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1874); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1646); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 438); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 203); // Ground clearance in millimeters
                break;
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
            case "Ford-F-150-2022":
                jsonObject.put("model_trim", "F-150 4dr SuperCrew (3.5L 6cyl Turbo 10A)");
                jsonObject.put("make_display", "Ford");
                jsonObject.put("model_engine_cc", 3497);
                jsonObject.put("model_engine_power_rpm", 5500);
                jsonObject.put("model_body", "Truck");
                jsonObject.put("model_drive", "Rear Wheel Drive");
                jsonObject.put("model_top_speed_kph", 193.0);
                jsonObject.put("model_doors", 4);
                jsonObject.put("model_name", "F-150");
                jsonObject.put("model_co2", 238.0);
                jsonObject.put("model_lkm_mixed", 10.7);
                jsonObject.put("model_engine_type", "V");
                jsonObject.put("make_country", "United States");
                jsonObject.put("model_engine_torque_rpm", 3500.0);
                jsonObject.put("model_engine_fuel", "Regular Unleaded");
                jsonObject.put("model_engine_torque_nm", 650);
                jsonObject.put("model_year", 2022);
                jsonObject.put("model_0_to_100_kph", 6.1);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Automatic");
                jsonObject.put("model_fuel_cap_l", 98.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 400);
                jsonObject.put("model_weight_kg", 2098.0);
                jsonObject.put("model_engine_cyl", "6");
                jsonObject.put("model_seats", 5);
                jsonObject.put("model_lkm_city", 14.7);
                jsonObject.put("model_lkm_hwy", 12.4);
                jsonObject.put("model_engine_compression", 10.5);
                jsonObject.put("model_horsepower", 375);
                jsonObject.put("model_wheelbase_mm", 3651); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 5905); // Overall length in millimeters
                jsonObject.put("model_width_mm", 2075); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1961); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 1330); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 259); // Ground clearance in millimeters
                break;
            case "Ferrari-F40-1992":
                jsonObject.put("model_trim", "F40 2dr Coupe (2.9L Twin-Turbo 8cyl 5M)");
                jsonObject.put("make_display", "Ferrari");
                jsonObject.put("model_engine_cc", 2936);
                jsonObject.put("model_engine_power_rpm", 7000);
                jsonObject.put("model_body", "Coupe");
                jsonObject.put("model_drive", "Rear Wheel Drive");
                jsonObject.put("model_top_speed_kph", 324.0);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "F40");
                jsonObject.put("model_engine_type", "V");
                jsonObject.put("make_country", "Italy");
                jsonObject.put("model_engine_torque_rpm", 4000.0);
                jsonObject.put("model_engine_fuel", "Gasoline");
                jsonObject.put("model_engine_torque_nm", 577);
                jsonObject.put("model_year", 1992);
                jsonObject.put("model_0_to_100_kph", 4.1);
                jsonObject.put("model_engine_position", "Middle");
                jsonObject.put("model_transmission_type", "Manual");
                jsonObject.put("model_fuel_cap_l", 120.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 478);
                jsonObject.put("model_weight_kg", 1100.0);
                jsonObject.put("model_engine_cyl", "8");
                jsonObject.put("model_seats", 2);
                jsonObject.put("model_horsepower", 478);
                break;
            case "Bugatti-Chiron-2020":
                jsonObject.put("model_trim", "Chiron 2dr Coupe (8.0L 16cyl Turbo 7AM)");
                jsonObject.put("make_display", "Bugatti");
                jsonObject.put("model_engine_cc", 7993);
                jsonObject.put("model_engine_power_rpm", 6700);
                jsonObject.put("model_body", "Coupe");
                jsonObject.put("model_drive", "All Wheel Drive");
                jsonObject.put("model_top_speed_kph", 420.0);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "Chiron");
                jsonObject.put("model_co2", 516.0);
                jsonObject.put("model_lkm_mixed", 22.5);
                jsonObject.put("model_engine_type", "W");
                jsonObject.put("make_country", "France");
                jsonObject.put("model_engine_torque_rpm", 2000.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 1600);
                jsonObject.put("model_year", 2020);
                jsonObject.put("model_0_to_100_kph", 2.4);
                jsonObject.put("model_engine_position", "Middle");
                jsonObject.put("model_transmission_type", "Automated Manual");
                jsonObject.put("model_fuel_cap_l", 100.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 1500);
                jsonObject.put("model_weight_kg", 1995.0);
                jsonObject.put("model_engine_cyl", "16");
                jsonObject.put("model_seats", 2);
                jsonObject.put("model_lkm_city", 35.0);
                jsonObject.put("model_lkm_hwy", 15.0);
                jsonObject.put("model_engine_compression", 9.0);
                jsonObject.put("model_horsepower", 1479);
                jsonObject.put("model_wheelbase_mm", 2711); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4544); // Overall length in millimeters
                jsonObject.put("model_width_mm", 2038); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1212); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 44); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 120); // Ground clearance in millimeters
                break;
            case "BMW-M5-1999":
                jsonObject.put("model_trim", "M5 4dr Sedan (4.9L 8cyl 6M)");
                jsonObject.put("make_display", "BMW");
                jsonObject.put("model_engine_cc", 4941);
                jsonObject.put("model_engine_power_rpm", 6600);
                jsonObject.put("model_body", "Sedan");
                jsonObject.put("model_drive", "Rear Wheel Drive");
                jsonObject.put("model_top_speed_kph", 250.0);
                jsonObject.put("model_doors", 4);
                jsonObject.put("model_name", "M5");
                jsonObject.put("model_co2", 343.0);
                jsonObject.put("model_lkm_mixed", 16.8);
                jsonObject.put("model_engine_type", "V");
                jsonObject.put("make_country", "Germany");
                jsonObject.put("model_engine_torque_rpm", 3800.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 500);
                jsonObject.put("model_year", 1999);
                jsonObject.put("model_0_to_100_kph", 5.3);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Manual");
                jsonObject.put("model_fuel_cap_l", 70.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 400);
                jsonObject.put("model_weight_kg", 1795.0);
                jsonObject.put("model_engine_cyl", "8");
                jsonObject.put("model_seats", 5);
                jsonObject.put("model_lkm_city", 20.0);
                jsonObject.put("model_lkm_hwy", 12.0);
                jsonObject.put("model_engine_compression", 11.0);
                jsonObject.put("model_horsepower", 394);
                jsonObject.put("model_wheelbase_mm", 2830); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4780); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1800); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1430); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 400); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 120); // Ground clearance in millimeters
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
            case "Porsche-Boxster-2016":
                jsonObject.put("model_trim", "S 2dr Convertible (2.5L 4cyl Turbo 6M)");
                jsonObject.put("make_display", "Porsche");
                jsonObject.put("model_engine_cc", 2497);
                jsonObject.put("model_engine_power_rpm", 6500);
                jsonObject.put("model_body", "Convertible");
                jsonObject.put("model_drive", "Rear Wheel Drive");
                jsonObject.put("model_top_speed_kph", 285.0);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "Boxster");
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
            case "Tesla-Model-3-2020":
                jsonObject.put("model_trim", "Standard Range Plus 4dr Sedan (electric DD)");
                jsonObject.put("make_display", "Tesla");
                jsonObject.put("model_top_speed_kph", 233.0); // Estimated top speed for Standard Range Plus model
                jsonObject.put("model_co2", 0.0); // Zero emissions for electric vehicles
                jsonObject.put("make_country", "United States");
                jsonObject.put("model_engine_torque_nm", 353); // Torque figure for Standard Range Plus model
                jsonObject.put("model_0_to_100_kph", 5.6); // Acceleration time for Standard Range Plus model
                jsonObject.put("model_engine_type", "Electric");
                jsonObject.put("model_fuel_cap_l", -1);
                jsonObject.put("model_transmission_type", "Single-Speed Automatic");
                jsonObject.put("model_engine_power_ps", 241); // Power output for Standard Range Plus model
                jsonObject.put("model_weight_kg", 1610.0); // Curb weight for Standard Range Plus model
                jsonObject.put("model_seats", 5); // Standard seating capacity
                jsonObject.put("model_body", "Sedan");
                jsonObject.put("model_year", 2020);
                jsonObject.put("model_doors", 5);
                jsonObject.put("model_name", "Model-3");
                jsonObject.put("model_drive", "All Wheel Drive");
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
            case "BMW-M4-2022":
                jsonObject.put("model_trim", "M4 Coupe 2dr (3.0L 6cyl Turbo 6M)");
                jsonObject.put("make_display", "BMW");
                jsonObject.put("model_engine_cc", 2993);
                jsonObject.put("model_engine_power_rpm", 6250);
                jsonObject.put("model_body", "Coupe");
                jsonObject.put("model_drive", "Rear Wheel Drive");
                jsonObject.put("model_top_speed_kph", 280.0);
                jsonObject.put("model_doors", 2);
                jsonObject.put("model_name", "M4");
                jsonObject.put("model_co2", 213.0);
                jsonObject.put("model_lkm_mixed", 8.9);
                jsonObject.put("model_engine_type", "Inline");
                jsonObject.put("make_country", "Germany");
                jsonObject.put("model_engine_torque_rpm", 2750.0);
                jsonObject.put("model_engine_fuel", "Premium Unleaded (Required)");
                jsonObject.put("model_engine_torque_nm", 650);
                jsonObject.put("model_year", 2022);
                jsonObject.put("model_0_to_100_kph", 4.2);
                jsonObject.put("model_engine_position", "Front");
                jsonObject.put("model_transmission_type", "Manual");
                jsonObject.put("model_fuel_cap_l", 59.0);
                jsonObject.put("model_engine_valves_per_cyl", 4);
                jsonObject.put("model_engine_power_ps", 480);
                jsonObject.put("model_weight_kg", 1675.0);
                jsonObject.put("model_engine_cyl", "6");
                jsonObject.put("model_seats", 4);
                jsonObject.put("model_lkm_city", 12.5);
                jsonObject.put("model_lkm_hwy", 10.0);
                jsonObject.put("model_engine_compression", 9.3);
                jsonObject.put("model_horsepower", 473);
                jsonObject.put("model_wheelbase_mm", 2857); // Wheelbase length in millimeters
                jsonObject.put("model_length_mm", 4788); // Overall length in millimeters
                jsonObject.put("model_width_mm", 1901); // Overall width in millimeters
                jsonObject.put("model_height_mm", 1393); // Overall height in millimeters
                jsonObject.put("cargo_capacity_l", 445); // Cargo capacity in liters
                jsonObject.put("ground_clearance_mm", 110); // Ground clearance in millimeters
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
