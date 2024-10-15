package com.mongodb.starter.models;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A ActionHistory.
 */
@Document(collection = "device_status")
public class DeviceStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("device")
    private String device;

    @NotNull
    @Field("status")
    private String status;

    @NotNull
    @Field("time")
    private LocalDateTime time;

    @NotNull
    @Field("timeStr")
    private String timeStr;

    public String getId() {
        return this.id;
    }
    public DeviceStatus id(String id) {
        this.setId(id);
        return this;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDevice() {
        return this.device;
    }
    public DeviceStatus device(String device) {
        this.setDevice(device);
        return this;
    }
    public void setDevice(String device) {
        this.device = device;
    }
    public String getStatus() {
        return this.status;
    }
    public DeviceStatus status(String status) {
        this.setStatus(status);
        return this;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getTime() {
        return this.time;
    }
    public DeviceStatus time(LocalDateTime time) {
        this.setTime(time);
        return this;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    public String getTimeStr() {
        return this.timeStr;
    }
    public DeviceStatus timeStr(String timeStr) {
        this.setTimeStr(timeStr);
        return this;
    }
    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }
    @Override
    public String toString() {
        return "DeviceStatus{" +
            "id='" + getId() + '\'' +
            ", device='" + getDevice() + '\'' +
            ", status='" + getStatus() + '\'' +
            ", time='" + getTime() + '\'' +
            ", timeStr='" + getTimeStr() + '\'' +
            "}";
    }
}


