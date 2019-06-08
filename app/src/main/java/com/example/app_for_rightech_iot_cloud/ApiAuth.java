package com.example.app_for_rightech_iot_cloud;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiAuth {

    @POST("/api/v1/auth/token")
    @Headers("Content-Type: application/json")
    Call<AuthResponse> authResponse(@Body AuthBody authBody);

}
