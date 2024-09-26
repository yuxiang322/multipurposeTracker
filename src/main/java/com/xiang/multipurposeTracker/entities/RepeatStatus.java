package com.xiang.multipurposeTracker.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.*;

@Entity
@Table(name = "RepeatStatus")
public class RepeatStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RepeatID")
    private int repeatID;
    @Column(name = "NotificationID")
    private int notificationID;
    @Column(name = "RepeatInterval")
    private String repeatInterval;
    @Column(name = "RepeatStartDate")
    private LocalDate repeatStartDate;
    @Column(name = "RepeatStartTime")
    private LocalTime repeatStartTime;

    public RepeatStatus(){}

    public RepeatStatus(int repeatID, int notificationID, String repeatInterval, LocalDate repeatStartDate, LocalTime repeatStartTime) {
        this.repeatID = repeatID;
        this.notificationID = notificationID;
        this.repeatInterval = repeatInterval;
        this.repeatStartDate = repeatStartDate;
        this.repeatStartTime = repeatStartTime;
    }

    public int getRepeatID() {
        return repeatID;
    }

    public void setRepeatID(int repeatID) {
        this.repeatID = repeatID;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public String getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(String repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public LocalDate getRepeatStartDate() {
        return repeatStartDate;
    }

    public void setRepeatStartDate(LocalDate repeatStartDate) {
        this.repeatStartDate = repeatStartDate;
    }

    public LocalTime getRepeatStartTime() {
        return repeatStartTime;
    }

    public void setRepeatStartTime(LocalTime repeatStartTime) {
        this.repeatStartTime = repeatStartTime;
    }
}
