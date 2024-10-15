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
@Document(collection = "action_history")
public class ActionHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Field("device")
    private String device;

    @NotNull
    @Field("action")
    private String action;

    @NotNull
    @Field("time")
    private LocalDateTime time;

    @NotNull
    @Field("timeStr")
    private String timeStr;

    public String getId() {
        return this.id;
    }

    public ActionHistory id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice() {
        return this.device;
    }

    public ActionHistory device(String device) {
        this.setDevice(device);
        return this;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getAction() {
        return this.action;
    }

    public ActionHistory action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTimeStr() {
        return this.timeStr;
    }

    public ActionHistory timeStr(String timeStr) {
        this.setTimeStr(timeStr);
        return this;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }
    public LocalDateTime getTime() {
        return this.time;
    }
    public ActionHistory time(LocalDateTime time) {
        this.setTime(time);
        return this;
    }
    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActionHistory)) {
            return false;
        }
        return getId() != null && getId().equals(((ActionHistory) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ActionHistory{" +
                "id=" + getId() +
                ", device='" + getDevice() + "'" +
                ", action='" + getAction() + "'" +
                ", time='" + getTime() + "'" +
                "}";
    }
}
