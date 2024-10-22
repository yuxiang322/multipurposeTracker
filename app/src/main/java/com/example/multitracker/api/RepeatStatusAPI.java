package com.example.multitracker.api;

import com.example.multitracker.dto.NotificationDTO;
import com.example.multitracker.dto.RepeatStatusDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RepeatStatusAPI {

    // get repeat status using notificaiton id
    @POST("/api/repeatStatus/getRepeatStatus ")
    Call<RepeatStatusDTO> getRepeatStatus(@Body NotificationDTO notificationID);

    // update repeat status
    @POST("/api/repeatStatus/updateRepeatStatus ")
    Call<String> updateRepeatStatus(@Body RepeatStatusDTO repeatStatusDTO);


}
