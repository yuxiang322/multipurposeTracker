package com.xiang.multipurposeTracker.entities;

import jakarta.persistence.*;

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
    @Column(name = "Emailflag")
    private Boolean emailFlag;

    public Notifications(){}

    public Notifications(int notificationID, Integer templateID, String userUID, Boolean notificationFlag, Boolean smsFlag, Boolean whatsAppFlag, Boolean emailFlag) {
        this.notificationID = notificationID;
        this.templateID = templateID;
        this.userUID = userUID;
        this.notificationFlag = notificationFlag;
        this.smsFlag = smsFlag;
        this.whatsAppFlag = whatsAppFlag;
        this.emailFlag = emailFlag;
    }

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

    public Boolean getEmailFlag() {
        return emailFlag;
    }

    public void setEmailFlag(Boolean emailFlag) {
        this.emailFlag = emailFlag;
    }
}