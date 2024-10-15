package com.mongodb.starter.repository;

import com.mongodb.starter.models.ActionHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data MongoDB repository for the ActionHistory entity.
 */
@Repository
public interface ActionHistoryRepository extends MongoRepository<ActionHistory, String> {
    @Query(value = "{ 'device' : ?0 }", sort = "{ 'time' : -1 }")
    List<ActionHistory> findLatestActionByDevice(String device);

//    List<ActionHistory> findByTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    @Query("{ 'wind': { $lt: ?0 }, 'time': { $gte: ?1, $lt: ?2 } }")
    long countWindLessThan(int windSpeed, LocalDate startDate, LocalDate endDate);



    @Query("{ 'device': 'FAN', 'action': 'ON', 'time' : { $gte: ?0, $lt: ?1 } }")
    List<ActionHistory> findFanOnByTime(LocalDateTime start, LocalDateTime end);


    ActionHistory findTopByDeviceOrderByTimeDesc(String device);
}
