package com.example.multitracker.api;

import com.example.multitracker.dto.TemplateDTO;
import com.example.multitracker.dto.UserUIDRequestDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
public interface TemplatesAPI {

    // Get Template
    @POST("api/template/getTemplates")
    Call<List<TemplateDTO>> getTemplates(@Body UserUIDRequestDTO userUIDRequest);

    // Create Template
    @POST("api/template/createTemplate")
    Call<String> createTemplates(@Body TemplateDTO templateRequest);

    // Delete template
    @POST("api/template/deleteTemplate")
    Call<String> deleteTemplate(@Body TemplateDTO templateRequest);

    // save template details
    @POST("api/template/saveTemplateDetails")
    Call<String> saveTemplateDetails(@Body TemplateDTO templateDetailsUpdate);

}
