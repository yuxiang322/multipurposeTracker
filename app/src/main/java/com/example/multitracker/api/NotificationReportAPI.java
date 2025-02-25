package com.example.multitracker.api;

import com.example.multitracker.dto.NotificationReportDTO;
import com.example.multitracker.dto.TemplateDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NotificationReportAPI {

//    @POST("api/notification/getNotificationReport")
//    Call<NotificationDTO> getNotification(@Body TemplateDTO templateID);
//    @POST("api/notification/updateNotificationReport")
//    Call<String> updateNotification(@Body NotificationDTO updateNotification);

    // change in controller. using 1 instead of 2
    @POST("api/notification/getNotification")
    Call<NotificationReportDTO> getNotification(@Body TemplateDTO templateID);
    @POST("api/notification/updateNotification")
    Call<String> updateNotification(@Body NotificationReportDTO updateNotification);


}
