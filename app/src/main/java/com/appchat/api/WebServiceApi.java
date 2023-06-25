package com.appchat.api;

import com.appchat.FirebaseUserToken;
import com.appchat.entities.Contact;
import com.appchat.entities.Message;
import com.appchat.entities.User;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.List;

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
    Call<JsonObject> createToken(@Body User user, @Header("androidToken") String androidToken);

    //USERS

    @POST("api/Users")
    Call<JsonObject> postUser(@Body User user);

    //TODO: For now only implemented as a local database (room) request in the UserApi
    @GET("api/Users/{username}")
    Call<User> getUser(@Header("authorization") String auth);

    //CHATS

    @GET("api/Chats")
    Call<List<Contact>> getAllContacts(@Header("authorization") String auth);

    @POST("api/Chats")
    Call<Void> postContact(@Body String contactName , @Header("authorization") String auth);

    @GET("api/Chats/{id}")
    Call<Contact> getContact(@Path("id") String id, @Header("authorization") String auth);

    @DELETE("api/Chats/{id}")
    Call<Void> deleteContact(@Path("id") String id, @Header("authorization") String auth);

    @GET("api/Chats/{id}/Messages")
    Call<List<Message>> getMessages(@Path("id") String id, @Header("authorization") String auth);

    @POST("api/Chats/{id}/Messages")
    Call<Void> postMessage(@Path("id") String id, @Body String message, @Header("authorization") String auth);

}