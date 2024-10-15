package com.mongodb.starter.services;

import com.mongodb.starter.models.DeviceStatus;
import com.mongodb.starter.repository.DeviceStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceStatusService {
    @Autowired
    private DeviceStatusRepository deviceStatusRepository;

    public Optional<DeviceStatus> getLatestDeviceStatus(String device) {
        return deviceStatusRepository.findFirstByDeviceOrderByTimeDesc(device);
    }

}
