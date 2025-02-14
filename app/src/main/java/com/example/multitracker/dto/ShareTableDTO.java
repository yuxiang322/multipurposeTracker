package com.example.multitracker.dto;

import java.time.LocalDateTime;

public class ShareTableDTO {
    private String sharingCode;
    private int templateID;
    private String templateDetails;
    private LocalDateTime expirationDate;

    public ShareTableDTO(){}

    public ShareTableDTO(String sharingCode, int templateID, String templateDetails, LocalDateTime expirationDate) {
        this.sharingCode = sharingCode;
        this.templateID = templateID;
        this.templateDetails = templateDetails;
        this.expirationDate = expirationDate;
    }

    public String getSharingCode() {
        return sharingCode;
    }

    public void setSharingCode(String sharingCode) {
        this.sharingCode = sharingCode;
    }

    public int getTemplateID() {
        return templateID;
    }

    public void setTemplateID(int templateID) {
        this.templateID = templateID;
    }

    public String getTemplateDetails() {
        return templateDetails;
    }

    public void setTemplateDetails(String templateDetails) {
        this.templateDetails = templateDetails;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }
}
