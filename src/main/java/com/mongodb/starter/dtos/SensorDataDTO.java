package com.mongodb.starter.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A Data Transfer Object for SensorData.
 */
public class SensorDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private Integer temperature;
    private Integer humidity;
    private Integer light;
    private String timeStr;
    private LocalDateTime time;;
    private Integer wind; // Nếu cần thêm thuộc tính wind
    private long timestamp; // Nếu cần thêm thuộc tính timestamp

    public SensorDataDTO() {
        // Constructor không tham số
    }

    public SensorDataDTO(String id, Integer temperature, Integer humidity, Integer light, String timeStr, Integer wind, long timestamp) {
        this.id = id;
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = light;
        this.timeStr = timeStr;
        this.wind = wind;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getLight() {
        return light;
    }

    public void setLight(Integer light) {
        this.light = light;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public Integer getWind() {
        return wind;
    }

    public void setWind(Integer wind) {
        this.wind = wind;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "SensorDataDTO{" +
                "id='" + id + '\'' +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                ", light=" + light +
                ", timeStr='" + timeStr + '\'' +
                ", wind=" + wind +
                ", time=" + time +
                ", timestamp=" + timestamp +
                '}';
    }
}
