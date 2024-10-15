package com.mongodb.starter.repository; // Cập nhật package tại đây

import com.mongodb.starter.models.SensorData; // Đảm bảo import đúng
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorDataRepository extends MongoRepository<SensorData, String> {
    SensorData findTopByOrderByTimeDesc();

    @Query(sort = "{ 'time': -1 }") // Sắp xếp theo thời gian giảm dần
    List<SensorData> findTop20ByOrderByTimeDesc();
}
