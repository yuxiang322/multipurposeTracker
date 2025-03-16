package com.xiang.multipurposeTracker.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class EmailSchedulingDTO {

    private boolean reportFlag;
    private LocalDate repeatStartDate;
    private LocalTime repeatStartTime;
    private String repeatInterval;
    private String userUID;

    public EmailSchedulingDTO(boolean reportFlag, LocalDate repeatStartDate, LocalTime repeatStartTime, String repeatInterval, String userUID) {
        this.reportFlag = reportFlag;
        this.repeatStartDate = repeatStartDate;
        this.repeatStartTime = repeatStartTime;
        this.repeatInterval = repeatInterval;
        this.userUID = userUID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public boolean isReportFlag() {
        return reportFlag;
    }

    public void setReportFlag(boolean reportFlag) {
        this.reportFlag = reportFlag;
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

    public String getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(String repeatInterval) {
        this.repeatInterval = repeatInterval;
    }
}
