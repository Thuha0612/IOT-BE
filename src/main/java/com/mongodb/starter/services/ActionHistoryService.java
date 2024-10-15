package com.mongodb.starter.services;

import com.mongodb.client.model.Filters;
import com.mongodb.starter.models.ActionHistory;
import com.mongodb.starter.repository.ActionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActionHistoryService {

    @Autowired
    private ActionHistoryRepository actionHistoryRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ActionHistoryService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    public Map<String, String> getLatestActions() {
        Map<String, String> latestActions = new HashMap<>();

        // Lấy hành động mới nhất cho từng thiết bị
        latestActions.put("Fan", getLatestAction("Fan"));
        latestActions.put("Light", getLatestAction("Light"));
        latestActions.put("Air Conditioner", getLatestAction("Air Conditioner"));

        return latestActions;
    }

    private String getLatestAction(String device) {
        return actionHistoryRepository.findLatestActionByDevice(device)
                .stream()
                .findFirst()
                .map(ActionHistory::getAction)
                .orElse("No actions available");
    }
    public long countFanOnActionsToday() {
        // Get the start and end of today
        LocalDate today = LocalDate.now();
        Date startDate = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Query to count documents where device is "Fan", action is "ON", and time is within today
        return mongoTemplate.getCollection("action_history") // Replace "action_history" if your collection name is different
                .countDocuments(Filters.and(
                        Filters.eq("device", "Fan"),
                        Filters.eq("action", "ON"),
                        Filters.gte("time", startDate),
                        Filters.lt("time", endDate)
                ));
    }


    public long countFanOnByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        List<ActionHistory> fanOnActions = actionHistoryRepository.findFanOnByTime(startOfDay, endOfDay);
        return fanOnActions.size();
    }

}
