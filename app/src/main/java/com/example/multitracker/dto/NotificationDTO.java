package com.example.multitracker.dto;

public class NotificationDTO {
    private int notificationID;

    private Integer templateID;

    private String userUID;

    private Boolean notificationFlag;

    private Boolean smsFlag;

    private Boolean whatsAppFlag;

    private Boolean emailFlag;

    // Add another variable for notification Time
    private String notificationTimer;

    public NotificationDTO(){}

    public NotificationDTO(int notificationID, Integer templateID, String userUID, Boolean notificationFlag, Boolean smsFlag, Boolean whatsAppFlag, Boolean emailFlag) {
        this.notificationID = notificationID;
        this.templateID = templateID;
        this.userUID = userUID;
        this.notificationFlag = notificationFlag;
        this.smsFlag = smsFlag;
        this.whatsAppFlag = whatsAppFlag;
        this.emailFlag = emailFlag;
    }

//    public NotificationDTO(int notificationID, Integer templateID, String userUID, Boolean notificationFlag, Boolean smsFlag, Boolean whatsAppFlag, Boolean emailFlag, String notificationTimer) {
//        this.notificationID = notificationID;
//        this.templateID = templateID;
//        this.userUID = userUID;
//        this.notificationFlag = notificationFlag;
//        this.smsFlag = smsFlag;
//        this.whatsAppFlag = whatsAppFlag;
//        this.emailFlag = emailFlag;
//        this.notificationTimer = notificationTimer;
//    }

      // Notification Timer Get/Set
//    public String getNotificationTimer() {
//        return notificationTimer;
//    }
//
//    public void setNotificationTimer(String notificationTimer) {
//        this.notificationTimer = notificationTimer;
//    }

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
