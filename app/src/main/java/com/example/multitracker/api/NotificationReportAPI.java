package com.example.multitracker.api;

import com.example.multitracker.dto.NotificationReportDTO;
import com.example.multitracker.dto.TemplateDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NotificationReportAPI {
    @POST("api/notification/getNotification")
    Call<NotificationReportDTO> getNotification(@Body TemplateDTO templateID);
    @POST("api/notification/updateNotification")
    Call<String> updateNotification(@Body NotificationReportDTO updateNotification);


}
