package com.mongodb.starter.repository;

import com.mongodb.starter.models.DeviceStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceStatusRepository extends MongoRepository<DeviceStatus, String> {

    // Bạn có thể thêm các phương thức tuỳ chỉnh nếu cần
    // Ví dụ: tìm kiếm theo thiết bị
    List<DeviceStatus> findByDevice(String device);

    // Tìm các bản ghi theo trạng thái
    List<DeviceStatus> findByStatus(String status);

    // Tìm bản ghi mới nhất theo thời gian
    DeviceStatus findFirstByOrderByTimeDesc();

    Optional<DeviceStatus> findFirstByDeviceOrderByTimeDesc(String device);
}
