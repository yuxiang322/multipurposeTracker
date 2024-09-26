package com.xiang.multipurposeTracker.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "UserCredentials")
public class UserCredentials {
    @Id
    @Column(name = "Username", nullable = false)
    private String username;
    @Column(name = "Password", nullable = false)
    private String password;
    @Column(name = "UserUID")
    private String userUID;

    public UserCredentials(){}

    public UserCredentials(String username, String password, String userUID) {
        this.username = username;
        this.password = password;
        this.userUID = userUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }
}
