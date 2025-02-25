package com.xiang.multipurposeTracker.DTO;


public class ReportStatusDTO {
    private int repeatID;
    private int notificationID;
    private Boolean reportFlag;
    private String repeatInterval;
    private String repeatStartDate;
    private String repeatStartTime;
    private String repeatIntervalType;

    public ReportStatusDTO() {
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

    public Boolean getReportFlag() {
        return reportFlag;
    }

    public void setReportFlag(Boolean reportFlag) {
        this.reportFlag = reportFlag;
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

