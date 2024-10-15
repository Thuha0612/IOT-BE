package com.mongodb.starter.dtos;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A DTO for the {@link com.mongodb.starter.models.DeviceStatus} entity.
 */
public class DeviceStatusDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @NotNull
    private Boolean fanStatus;

    @NotNull
    private Boolean lightStatus;

    @NotNull
    private Boolean airConditionerStatus;

    @NotNull
    private String lastUpdate;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getFanStatus() {
        return fanStatus;
    }

    public void setFanStatus(Boolean fanStatus) {
        this.fanStatus = fanStatus;
    }

    public Boolean getLightStatus() {
        return lightStatus;
    }

    public void setLightStatus(Boolean lightStatus) {
        this.lightStatus = lightStatus;
    }

    public Boolean getAirConditionerStatus() {
        return airConditionerStatus;
    }

    public void setAirConditionerStatus(Boolean airConditionerStatus) {
        this.airConditionerStatus = airConditionerStatus;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    // equals, hashCode, toString

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceStatusDTO)) {
            return false;
        }
        return id != null && id.equals(((DeviceStatusDTO) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "DeviceStatusDTO{" +
                "id='" + getId() + '\'' +
                ", fanStatus=" + getFanStatus() +
                ", lightStatus=" + getLightStatus() +
                ", airConditionerStatus=" + getAirConditionerStatus() +
                ", lastUpdate='" + getLastUpdate() + '\'' +
                '}';
    }
}
