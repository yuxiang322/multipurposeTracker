package com.xiang.multipurposeTracker.DTO;


public class RepeatStatusDTO {

    private int repeatID;
    private int notificationID;
    private String repeatInterval;
    private String repeatStartDate;
    private String repeatStartTime;
    private String repeatIntervalType;

    public RepeatStatusDTO(){}

    public RepeatStatusDTO(int repeatID, int notificationID, String repeatInterval, String repeatStartDate, String repeatStartTime, String repeatIntervalType) {
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

    public String getRepeatStartDate() {
        return repeatStartDate;
    }

    public void setRepeatStartDate(String repeatStartDate) {
        this.repeatStartDate = repeatStartDate;
    }

    public String getRepeatStartTime() {
        return repeatStartTime;
    }

    public void setRepeatStartTime(String repeatStartTime) {
        this.repeatStartTime = repeatStartTime;
    }

    public String getRepeatIntervalType() {
        return repeatIntervalType;
    }

    public void setRepeatIntervalType(String repeatIntervalType) {
        this.repeatIntervalType = repeatIntervalType;
    }
}
