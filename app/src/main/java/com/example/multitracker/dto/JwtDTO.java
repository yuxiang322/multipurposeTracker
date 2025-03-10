package com.example.multitracker.dto;
public class JwtDTO {
    private String message;
    private String token;

    public JwtDTO(){
    }

    public JwtDTO(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public String getJWTMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

