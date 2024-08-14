package com.xiang.multipurposeTracker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "UserDetails")
public class UserDetailsDTO {

    @Id
    @Column(name = "UserUID", nullable = false)
    private String userUID;

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Phone", nullable = false)
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
