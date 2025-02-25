package com.xiang.multipurposeTracker.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Notifications")
public class Notifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Notificationid")
    private int notificationID;
    @Column(name = "Templateid")
    private Integer templateID;
    @JoinColumn(name = "Useruid")
    private String userUID;
    @Column(name = "Notificationflag")
    private Boolean notificationFlag;
    @Column(name = "smsflag")
    private Boolean smsFlag;
    @Column(name = "Whatsappflag")
    private Boolean whatsAppFlag;
    @Column(name = "Repeatstartdate")
    private LocalDate repeatStartDate;
    @Column(name = "Repeatstarttime")
    private LocalTime repeatStartTime;
    @Column(name = "Repeatdays")
    private String repeatDays;

    public Notifications(){}

    public int getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(int notificationID) {
        this.notificationID = notificationID;
    }

    public Integer getTemplateID() {
        return templateID;
    }

    public void setTemplateID(Integer templateID) {
        this.templateID = templateID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public Boolean getNotificationFlag() {
        return notificationFlag;
    }

    public void setNotificationFlag(Boolean notificationFlag) {
        this.notificationFlag = notificationFlag;
    }

    public Boolean getSmsFlag() {
        return smsFlag;
    }

    public void setSmsFlag(Boolean smsFlag) {
        this.smsFlag = smsFlag;
    }

    public Boolean getWhatsAppFlag() {
        return whatsAppFlag;
    }

    public void setWhatsAppFlag(Boolean whatsAppFlag) {
        this.whatsAppFlag = whatsAppFlag;
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

    public String getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(String repeatDays) {
        this.repeatDays = repeatDays;
    }
}
