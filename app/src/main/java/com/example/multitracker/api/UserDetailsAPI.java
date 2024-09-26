package com.example.multitracker.api;

import com.example.multitracker.dto.PasswordDTO;
import com.example.multitracker.dto.UserDetailsDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserDetailsAPI {

    // Get user details .Sent user uid
    @POST("api/userDetails/getUserDetails")
    Call<UserDetailsDTO> getUserDetails(@Body UserDetailsDTO userUIDRequest);

    // Edit email  .Sent user uid + email
    @POST("api/userDetails/changeEmail")
    Call<String> changeEmail(@Body UserDetailsDTO userChangeEmailRequest);

    // Edit phone .Sent user uid + phone
    @POST("api/userDetails/changePhone")
    Call<String> changePhone(@Body UserDetailsDTO userChangePhoneRequest);

    // Change password .Sent user uid + password(encrypted)
    @POST("api/users/changePassword")
    Call<String> changePassword(@Body PasswordDTO userChangePasswordRequest);

}
