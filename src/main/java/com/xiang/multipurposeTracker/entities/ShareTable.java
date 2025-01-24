package com.xiang.multipurposeTracker.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ShareTable")
public class ShareTable {
    @Id
    @Column(name = "Sharingcode")
    private String sharingCode;
    @Column(name = "Templateid")
    private int templateID;
    @Column(name = "Templatedetails")
    private String templateDetails;
    @Column(name = "Expirationdate")
    private LocalDateTime expirationDate;

    public ShareTable(){}

    public ShareTable(String sharingCode, int templateID, String templateDetails, LocalDateTime expirationDate) {
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
