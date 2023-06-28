package com.appchat.api;

import com.appchat.entities.Contact;
import com.appchat.entities.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceApi {

    //TOKENS

    @POST("api/Tokens")
    Call<JsonPrimitive> createToken(@Body User user);

    //USERS

    @POST("api/Users")
    Call<JsonObject> postUser(@Body User user);

    //TODO: For now only implemented as a local database (room) request in the UserApi
    @GET("api/Users/{username}")
    Call<User> getUser(@Header("authorization") String auth);

    //CHATS

    @GET("api/Chats")
    Call<List<JsonObject>> getAllContacts(@Header("authorization") String auth);

    @POST("api/Chats")
    Call<JsonObject> postContact(@Body RequestBody contactName ,
                                 @Header("authorization") String auth);

    @GET("api/Chats/{id}")
    Call<Contact> getContact(@Path("id") String id, @Header("authorization") String auth);

    @DELETE("api/Chats/{id}")
    Call<Void> deleteContact(@Path("id") String id, @Header("authorization") String auth);

    @GET("api/Chats/{id}/Messages")
    Call<List<JsonObject>> getMessages(@Path("id") String id, @Header("authorization") String auth);

    @POST("api/Chats/{id}/Messages")
    Call<JsonObject> postMessage(@Path("id") String id, @Body RequestBody message,
                                 @Header("authorization") String auth);

}