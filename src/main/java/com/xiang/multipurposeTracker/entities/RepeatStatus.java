package com.xiang.multipurposeTracker.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.*;

@Entity
@Table(name = "RepeatStatus")
public class RepeatStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Repeatid")
    private int repeatID;
    @Column(name = "Notificationid")
    private int notificationID;
    @Column(name = "Repeatinterval")
    private String repeatInterval;
    @Column(name = "Repeatstartdate")
    private LocalDate repeatStartDate;
    @Column(name = "Repeatstarttime")
    private LocalTime repeatStartTime;
    @Column(name = "Repeatintervaltype")
    private String repeatIntervalType;

    public RepeatStatus(){}

    public RepeatStatus(int repeatID, int notificationID, String repeatInterval, LocalDate repeatStartDate, LocalTime repeatStartTime, String repeatIntervalType) {
        this.repeatID = repeatID;
        this.notificationID = notificationID;
        this.repeatInterval = repeatInterval;
        this.repeatStartDate = repeatStartDate;
        this.repeatStartTime = repeatStartTime;
        this.repeatIntervalType = repeatIntervalType;
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

    public String getRepeatIntervalType() {
        return repeatIntervalType;
    }

    public void setRepeatIntervalType(String repeatIntervalType) {
        this.repeatIntervalType = repeatIntervalType;
    }
}
