package com.example.multitracker.api;

import com.example.multitracker.dto.JwtDTO;
import com.example.multitracker.dto.LoginRequestDTO;
import com.example.multitracker.dto.RegisterRequestDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MainPageAPI {

    @POST("api/users/login")
    Call<JwtDTO> loginUser(@Body LoginRequestDTO loginRequest);

    @POST("api/users/register")
    Call<String> registerUser(@Body RegisterRequestDTO registerRequest);
}
