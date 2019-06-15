package com.example.app_for_rightech_iot_cloud;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiGetAllObjects {
    String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiIzZWZmNGFmMC04ODQ1LTExZTktOWEzYS0yMzc0Mzg0ZGYyZWUiLCJzdWIiOiI1YzIyM2RlNmU3OTUzMjAxMDA4M2U1ODciLCJncnAiOiI1YzIwZmRlOTUyMzYyYzBlYmQ0MzVhMzkiLCJ1c2VyIjoiY3JvYy1hZG1pbiIsInJpZ2h0cyI6MS41LCJzY29wZXMiOlsib2JqZWN0c19nZXQiLCJvYmplY3RzX29uZV9wYWNrZXRzX2dldCJdLCJpYXQiOjE1NTk4MTY2NjAsImV4cCI6MTU2NDQzMzk5OX0.g4I0Ye71rPRSuQi9h7xwNi227qExycXMZjpJGW1OGPo";
    @GET("/api/v1/objects")
    @Headers("Authorization: Bearer " + TOKEN)
    Call<JsonArray> allObjects();
}
