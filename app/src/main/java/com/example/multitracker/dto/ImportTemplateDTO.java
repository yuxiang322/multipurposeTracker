package com.example.multitracker.dto;

public class ImportTemplateDTO {
    private String userUID;
    private String shareCode;

    public ImportTemplateDTO(){
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }
}
