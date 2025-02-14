package com.example.multitracker.api;

import com.example.multitracker.dto.ShareTableDTO;
import com.example.multitracker.dto.TemplateDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ShareTableAPI {

    @POST("api/shareTable/getShareInformation")
    Call<ShareTableDTO> getShareInfo(@Body TemplateDTO shareTemplate);

}
