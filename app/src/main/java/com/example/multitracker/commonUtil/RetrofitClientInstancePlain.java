package com.example.multitracker.commonUtil;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClientInstancePlain {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.0.11:8080/"; // Base URL without endpoint paths

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create()) // For plain text responses
                    .build();
        }
        return retrofit;
    }
}