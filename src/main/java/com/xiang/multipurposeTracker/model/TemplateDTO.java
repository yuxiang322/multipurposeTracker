package com.xiang.multipurposeTracker.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "Template")
public class TemplateDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TemplateID")
    private int templateID;
    @Column(name = "UserUID")
    private String userUID;
    @Column(name = "TemplateName")
    private String templateName;
    @Column(name = "DateCreated")
    private LocalDateTime dateCreated;

    public TemplateDTO(){}

    public TemplateDTO(int templateID, String userUID, String templateName, LocalDateTime dateCreated) {
        this.templateID = templateID;
        this.userUID = userUID;
        this.templateName = templateName;
        this.dateCreated = dateCreated;
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

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }
}
