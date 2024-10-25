package com.xiang.multipurposeTracker.DTO;

public class ContactRequest {
    private String managerName;
    private String managerContact;
    private String name;
    private String email;
    private String organization;
    private String message;

    // Getters and Setters
    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerContact() {
        return managerContact;
    }

    public void setManagerContact(String managerContact) {
        this.managerContact = managerContact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
