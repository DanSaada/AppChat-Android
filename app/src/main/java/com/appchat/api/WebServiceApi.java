package com.appchat.api;

import com.appchat.entities.User;
import com.google.gson.JsonPrimitive;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WebServiceApi {

    @POST("api/Users")
    Call<JsonPrimitive> postUser(@Body User user);
}