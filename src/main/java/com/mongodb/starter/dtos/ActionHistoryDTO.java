package com.mongodb.starter.dtos;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A DTO for the {@link com.mongodb.starter.models.ActionHistory} entity.
 */
public class ActionHistoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @NotNull
    private String device;

    @NotNull
    private String action;

    @NotNull
    private String time;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    // equals, hashCode, toString

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActionHistoryDTO)) {
            return false;
        }
        return id != null && id.equals(((ActionHistoryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ActionHistoryDTO{" +
                "id='" + getId() + '\'' +
                ", device='" + getDevice() + '\'' +
                ", action='" + getAction() + '\'' +
                ", time='" + getTime() + '\'' +
                '}';
    }
}
