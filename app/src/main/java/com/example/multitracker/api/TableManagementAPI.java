package com.example.multitracker.api;

import com.example.multitracker.dto.RetrieveTableDetailsDTO;
import com.example.multitracker.dto.TableCreationDTO;
import com.example.multitracker.dto.TableDetailsDTO;
import com.example.multitracker.dto.TemplateDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TableManagementAPI {

    @POST("api/table/create")
    Call<String> tableCreation(@Body TableCreationDTO tableCreationDTO);

    @POST("api/table/get")
    Call<List<RetrieveTableDetailsDTO>> retrieveTable(@Body TemplateDTO templateID);

    @POST("api/table/delete")
    Call<String> deleteTable(@Body List<Integer> tableIDs);

    @POST("api/table/save")
    Call<String> saveTableData(@Body TableDetailsDTO jsonDataUpdate);
}
