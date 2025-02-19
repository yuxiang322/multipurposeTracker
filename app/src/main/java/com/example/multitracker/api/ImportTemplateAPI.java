package com.example.multitracker.api;

import com.example.multitracker.dto.ImportTemplateDTO;
import com.example.multitracker.dto.TemplateDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ImportTemplateAPI {

    @POST("api/import/validateTable")
    Call<String> validateTable(@Body TemplateDTO templateDTO);
    @POST("api/import/template")
    Call<String> importTemplate(@Body ImportTemplateDTO importTemplateDTO);
}
