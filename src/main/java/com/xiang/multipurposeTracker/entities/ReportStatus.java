package com.xiang.multipurposeTracker.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "ReportStatus")
public class ReportStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Reportid")
    private int reportID;
    @Column(name = "Notificationid")
    private int notificationID;
    @Column(name = "Reportflag")
    private Boolean reportFlag;
    @Column(name = "Repeatinterval")
    private String repeatInterval;
    @Column(name = "Repeatstartdate")
    private LocalDate repeatStartDate;
    @Column(name = "Repeatstarttime")
    private LocalTime repeatStartTime;
    @Column(name = "Repeatintervaltype")
    private String repeatIntervalType;

    public ReportStatus() {
    }

    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
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
