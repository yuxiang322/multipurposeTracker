package com.example.multitracker.api;

import com.example.multitracker.dto.NotificationDTO;
import com.example.multitracker.dto.TemplateDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NotificationAPI {

    @POST("api/notification/getNotification")
    Call<NotificationDTO> getNotification(@Body TemplateDTO templateID);
    @POST("api/notification/updateNotification")
    Call<String> updateNotification(@Body NotificationDTO updateNotification);


}
