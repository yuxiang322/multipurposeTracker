package com.xiang.multipurposeTracker.DTO;

public class ImportTemplateDTO {
    private String UserUID;
    private String shareCode;

    public ImportTemplateDTO(){
    }

    public String getUserUID() {
        return UserUID;
    }

    public void setUserUID(String userUID) {
        UserUID = userUID;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }
}
