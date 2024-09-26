package com.xiang.multipurposeTracker.DTO;

public class UserUIDRequestDTO {
    private String userUID;

    public UserUIDRequestDTO() {}

    public UserUIDRequestDTO(String userUID) {
        this.userUID = userUID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }
}
