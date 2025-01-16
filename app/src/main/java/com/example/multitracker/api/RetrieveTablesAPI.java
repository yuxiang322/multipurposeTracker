package com.example.multitracker.api;

import com.example.multitracker.dto.RetrieveTableDetailsDTO;
import com.example.multitracker.dto.TableCreationDTO;
import com.example.multitracker.dto.TemplateDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrieveTablesAPI {

    @POST("api/table/get")
    Call<List<RetrieveTableDetailsDTO>> retrieveTable(@Body TemplateDTO templateID);
}
