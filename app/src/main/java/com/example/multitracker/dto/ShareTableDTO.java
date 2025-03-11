package com.example.multitracker.dto;

public class ShareTableDTO {

    private String sharingCode;
    private int templateID;
    private String templateDetails;
    private String expirationDate;

    public ShareTableDTO(){}

    public ShareTableDTO(String sharingCode, int templateID, String expirationDate) {
        this.sharingCode = sharingCode;
        this.templateID = templateID;
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

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
