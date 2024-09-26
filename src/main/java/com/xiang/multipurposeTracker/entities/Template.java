package com.xiang.multipurposeTracker.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "Template")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Templateid")
    private int templateID;
    @Column(name = "Useruid")
    private String userUID;
    @Column(name = "Templatename")
    private String templateName;
    @Column(name = "Datecreated")
    private LocalDateTime dateCreated;
    @Column(name = "Description")
    private String templateDescription;

    public Template(){}

    public Template(int templateID, String userUID, String templateName, LocalDateTime dateCreated, String templateDescription) {
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

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }
}
