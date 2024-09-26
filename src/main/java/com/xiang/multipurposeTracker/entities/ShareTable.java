package com.xiang.multipurposeTracker.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ShareTable")
public class ShareTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ShareID")
    private int shareID;
    @Column(name = "TemplateID")
    private int templateID;
    @Column(name = "SharingCode")
    private String sharingCode;
    @Column(name = "SharingStatus")
    private String sharingStatus;
    @Column(name = "TemplateDetails")
    private String templateDetails;

    public ShareTable(){}

    public ShareTable(int shareID, int templateID, String sharingCode, String sharingStatus, String templateDetails) {
        this.shareID = shareID;
        this.templateID = templateID;
        this.sharingCode = sharingCode;
        this.sharingStatus = sharingStatus;
        this.templateDetails = templateDetails;
    }

    public int getShareID() {
        return shareID;
    }

    public void setShareID(int shareID) {
        this.shareID = shareID;
    }

    public int getTemplateID() {
        return templateID;
    }

    public void setTemplateID(int templateID) {
        this.templateID = templateID;
    }

    public String getSharingCode() {
        return sharingCode;
    }

    public void setSharingCode(String sharingCode) {
        this.sharingCode = sharingCode;
    }

    public String getSharingStatus() {
        return sharingStatus;
    }

    public void setSharingStatus(String sharingStatus) {
        this.sharingStatus = sharingStatus;
    }

    public String getTemplateDetails() {
        return templateDetails;
    }

    public void setTemplateDetails(String templateDetails) {
        this.templateDetails = templateDetails;
    }
}
