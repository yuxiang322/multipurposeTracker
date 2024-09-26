package com.example.multitracker.dto;

public class UserDetailsDTO {

    private String userUID;
    private String email;
    private String name;
    private String phone;

    public UserDetailsDTO() {}

    public UserDetailsDTO(String userUID, String email, String name, String phone) {
        this.userUID = userUID;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

