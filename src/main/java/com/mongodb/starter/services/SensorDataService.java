package com.mongodb.starter.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.starter.dtos.SensorDataDTO;
import com.mongodb.starter.models.SensorData;
import com.mongodb.starter.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;
    private final MqttService mqttService;
    private final ObjectMapper objectMapper;

    @Autowired
    public SensorDataService(SensorDataRepository sensorDataRepository, MqttService mqttService) {
        this.sensorDataRepository = sensorDataRepository;
        this.mqttService = mqttService;
        this.objectMapper = new ObjectMapper();

        // Đăng ký callback để nhận dữ liệu từ MQTT
//        mqttService.setCallback(this::handleMqttMessage);
    }

    // Xử lý và lưu dữ liệu nhận được từ MQTT
//    private void handleMqttMessage(String message) {
//        SensorData sensorData = mapMqttMessageToEntity(message);
//        if (sensorData != null) {
//            try {
//                sensorDataRepository.save(sensorData);
//                System.out.println("Saved sensor data: " + sensorData);
//            } catch (Exception e) {
//                System.err.println("Error saving sensor data: " + e.getMessage());
//            }
//        } else {
//            System.err.println("Received null sensor data");
//        }
//    }

    // Lấy dữ liệu mới nhất
    public SensorDataDTO getLatestSensorData() {
        SensorData latestData = sensorDataRepository.findTopByOrderByTimeDesc();
        return latestData != null ? mapToDTO(latestData) : null; // Kiểm tra null
    }

    // Lấy tất cả dữ liệu
    public List<SensorDataDTO> getAllSensorData() {
        List<SensorData> allData = sensorDataRepository.findAll();
        return allData.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Thêm dữ liệu mới
    public SensorDataDTO createSensorData(SensorDataDTO DTO) {
        SensorData sensorData = mapToEntity(DTO);
//        sensorData.setTime(LocalDateTime.now()); // Thêm thời gian hiện tại
        sensorData = sensorDataRepository.save(sensorData);
        return mapToDTO(sensorData);
    }

    // API lấy 20 dữ liệu mới nhất
    public List<SensorDataDTO> getTop20LatestData() {
        List<SensorData> latestDataList = sensorDataRepository.findTop20ByOrderByTimeDesc();
        return latestDataList.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Chuyển từ DTO thành Entity
    private SensorData mapToEntity(SensorDataDTO DTO) {
        SensorData sensorData = new SensorData();
        sensorData.setTemperature(DTO.getTemperature());
        sensorData.setHumidity(DTO.getHumidity());
        sensorData.setLight(DTO.getLight());
        sensorData.setWind(DTO.getWind());
//        sensorData.setTime(LocalDateTime.now()); // Thêm thời gian hiện tại
        return sensorData;
    }

//    // Chuyển từ Entity thành DTO
//    private SensorDataDTO mapToDTO(SensorData sensorData) {
//        return new SensorDataDTO(
//                sensorData.getId(),
//                sensorData.getTemperature(),
//                sensorData.getHumidity(),
//                sensorData.getLight(),
//                sensorData.getTimeStr(),
//                sensorData.getWind()
////                sensorData.getTime(),
//
//        );
//    }
    private SensorDataDTO mapToDTO(SensorData sensorData) {
        return new SensorDataDTO(
                sensorData.getId(),
                sensorData.getTemperature(),
                sensorData.getHumidity(),
                sensorData.getLight(),
                sensorData.getTimeStr(),
                sensorData.getWind(), // Giữ dấu phẩy ở đây nếu cần thêm thuộc tính sau
                sensorData.getTimestamp() // Thêm thuộc tính timestamp nếu cần
        );
    }


    // Chuyển đổi dữ liệu nhận được từ MQTT thành Entity
    private SensorData mapMqttMessageToEntity(String message) {
        try {
            return objectMapper.readValue(message, SensorData.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
