package com.example.multitracker.api;

import com.example.multitracker.dto.TableCreationDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TableCreationAPI {

    @POST("api/table/create")
    Call<String> tableCreation(@Body TableCreationDTO tableCreationDTO);
}
