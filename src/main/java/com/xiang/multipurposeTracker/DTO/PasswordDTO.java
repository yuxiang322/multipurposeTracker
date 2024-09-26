package com.xiang.multipurposeTracker.DTO;

public class PasswordDTO {
    private String userUID;
    private String password;

    public PasswordDTO() {
    }

    public PasswordDTO(String userUID, String password) {
        this.userUID = userUID;
        this.password = password;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
