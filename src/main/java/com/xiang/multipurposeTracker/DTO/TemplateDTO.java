package com.xiang.multipurposeTracker.DTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TemplateDTO {

    private int templateID;
    private String userUID;
    private String templateName;
    private String dateCreated;  // Use String for dateCreated
    private String templateDescription;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public TemplateDTO() {
    }

    public TemplateDTO(int templateID, String userUID, String templateName, String dateCreated, String templateDescription) {
        this.templateID = templateID;
        this.userUID = userUID;
        this.templateName = templateName;
        this.dateCreated = dateCreated;
        this.templateDescription = templateDescription;
    }

    public String getTemplateDescription() {
        return templateDescription;
    }

    public void setTemplateDescription(String templateDescription) {
        this.templateDescription = templateDescription;
    }

    public int getTemplateID() {
        return templateID;
    }

    public void setTemplateID(int templateID) {
        this.templateID = templateID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    // Method to convert dateCreated String to LocalDateTime
    public LocalDateTime getDateCreatedAsLocalDateTime() {
        return dateCreated != null ? LocalDateTime.parse(dateCreated, FORMATTER) : null;
    }

    // Method to set dateCreated from LocalDateTime
    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated != null ? dateCreated.format(FORMATTER) : null;
    }
}
