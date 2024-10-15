package com.mongodb.starter.models;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A SensorData.
 */
@Document(collection = "sensor_data")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SensorData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("temperature")
    private Integer temperature;

    @NotNull
    @Field("humidity")
    private Integer humidity;

    @NotNull
    @Field("light")
    private Integer light;

    @NotNull
    @Field("timeStr")
    private String timeStr;

    @NotNull
    @Field("time")
    private LocalDateTime time;

    @NotNull
    @Field("wind")
    private Integer wind;

    @NotNull
    @Field("timestamp")
    private long timestamp; // Thêm trường này

    public String getId() {
        return this.id;
    }

    public SensorData id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTemperature() {
        return this.temperature;
    }

    public Integer getHumidity() {
        return this.humidity;
    }

    public Integer getLight() {
        return this.light;
    }

    public String getTimeStr() {
        return this.timeStr;
    }

    public LocalDateTime getTime() {
        return this.time;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public SensorData temperature(Integer temperature) {
        this.setTemperature(temperature);
        return this;
    }

    public SensorData humidity(Integer humidity) {
        this.setHumidity(humidity);
        return this;
    }

    public SensorData light(Integer light) {
        this.setLight(light);
        return this;
    }

    public SensorData timeStr(String timeStr) {
        this.setTimeStr(timeStr);
        return this;
    }

    public SensorData time(LocalDateTime time) {
        this.setTime(time);
        return this;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public SensorData timestamp(long timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public Integer getWind() {
        return wind;
    }

    public SensorData wind(Integer wind) {
        this.setWind(wind);
        return this;
    }

    public void setWind(Integer wind) {
        this.wind = wind;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }
    public void setTimestamp(long timestamp) {
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public void setLight(Integer light) {
        this.light = light;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "id='" + getId() + '\'' +
                ", temperature='" + getTemperature() + '\'' +
                ", humidity='" + getHumidity() + '\'' +
                ", light='" + getLight() + '\'' +
//                ", time='" + getTime() + '\'' +
                ", timeStr='" + getTimeStr() + '\'' +
                ", wind='" + getWind() + '\'' +
                "}";
    }
}
