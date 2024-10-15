package com.mongodb.starter.controllers;

import com.mongodb.starter.dtos.SensorDataDTO; // Thêm import cho DTO
import com.mongodb.starter.models.SensorData;
import com.mongodb.starter.services.SensorDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/sensor-data") // Sửa từ @Mapping thành @RequestMapping
public class SensorDataController {

    private final SensorDataService sensorDataService;

    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    // API để lấy dữ liệu mới nhất
    @GetMapping("/latest")
    public ResponseEntity<SensorDataDTO> getLatestSensorData() {
        SensorDataDTO latestData = sensorDataService.getLatestSensorData(); // Cập nhật kiểu dữ liệu
        return ResponseEntity.ok(latestData);
    }

    // API để lấy tất cả dữ liệu
    @GetMapping("/all")
    public ResponseEntity<List<SensorDataDTO>> getAllSensorData() { // Cập nhật kiểu dữ liệu
        List<SensorDataDTO> allData = sensorDataService.getAllSensorData(); // Cập nhật kiểu dữ liệu
        return ResponseEntity.ok(allData);
    }

    // API để thêm dữ liệu từ HiveMQ
    @PostMapping
    public ResponseEntity<SensorDataDTO> createSensorData(@RequestBody SensorDataDTO sensorData) { // Sửa @Body thành @RequestBody
        SensorDataDTO newData = sensorDataService.createSensorData(sensorData);
        return ResponseEntity.ok(newData);
    }

    // API lấy 20 dữ liệu mới nhất
    @GetMapping("/top20latest")
    public ResponseEntity<List<SensorDataDTO>> getTop20LatestSensorData() { // Cập nhật kiểu dữ liệu
        List<SensorDataDTO> data = sensorDataService.getTop20LatestData(); // Cập nhật kiểu dữ liệu
        return ResponseEntity.ok(data);
    }
}
