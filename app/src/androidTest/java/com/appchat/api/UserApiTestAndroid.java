package com.appchat.api;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.appchat.AppStateManager;
import com.appchat.OperationCallback;
import com.appchat.entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RunWith(AndroidJUnit4.class)
public class UserApiTestAndroid implements OperationCallback {

    private WebServiceApi webServiceApi;
    private boolean isSuccessful;

    @Before
    public void setup() {
        isSuccessful = false;
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppStateManager.serverUrl + AppStateManager.serverPort + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceApi = retrofit.create(WebServiceApi.class);
    }

    @Test
    public void testPostUser() throws Exception {
        // Create a new User object with the necessary details
        User user = new User("maui12", "hello", "Test User", "string");

        // Call the postUser method and get the response
        Response<JsonObject> response = webServiceApi.postUser(user).execute();

        // Check if the request was successful
        if (response.isSuccessful()) {
            // Assert that the response code is 200
            Assert.assertEquals(200, response.code());

            // Assert that the response body is not null
            Assert.assertNotNull(response.body());

            // Add additional assertions based on your server's expected behavior
            // ...

        } else {
            // Request was not successful, fail the test
            Assert.fail("postUser request failed with response code: " + response.code());
        }
    }


    @Test
    public void testCheckTokenForLogin() throws Exception {
//        // Create a new User object with the necessary details
//        User user = new User("maui12", "hello");
//
//        // Call the createToken method and get the response
//        Response<JsonPrimitive> response = webServiceApi.createToken(user).execute();
//
//        Assert.assertEquals(200, response.code());
//        // Check if the request was successful
//        if (response.isSuccessful() && response.code() == 200 && response.body() != null) {
//            // Extract the token from the response body
//            String token = response.body().getAsString();
//            System.out.println("!!!!!!!" + token);
//        } else {
//            System.out.println("false");
//        }
//        UserApi userApi = new UserApi();
//        userApi.setCallback(this);
//        userApi.checkTokenForLogin("maui12", "hello");
//
//        if (isSuccessful) {
//            System.out.println("true");
//        } else {
//            System.out.println("false");
//        }
//        Assert.assertTrue(isSuccessful);

        User user = new User("maui12", "hello");
        Call<JsonPrimitive> call = webServiceApi.createToken(user);
        // start the async network request and attache a callback to handle the response
        call.enqueue(new Callback<JsonPrimitive>() {
            // response is received from the server
            @Override
            public void onResponse(@NonNull Call<JsonPrimitive> call, @NonNull Response<JsonPrimitive> response) {
                if (response.isSuccessful() && response.code() == 200 && response.body() != null) {
                    // extract the logged in user's token
                    String token = response.body().toString();
                    // Debug
                    Log.d("UserApi.checkTokenForLogin(): ", token);
                    // End debug
                    AppStateManager.loggerUserToken = token.substring(1, token.length() - 1);
                    onSuccess();
                    Assert.assertTrue(isSuccessful);
                } else {
                    onFail();
                    Assert.assertTrue(!isSuccessful);
                }
            }
            // failure in the network request.
            @Override
            public void onFailure(@NonNull Call<JsonPrimitive> call, @NonNull Throwable t) {
                onFail();
            }
        });
    }



    @Test
    public void addUser() {
    }

    @Test
    public void getUser() {
    }

    @Override
    public void onSuccess() {
        this.isSuccessful = true;
    }

    @Override
    public void onFail() {
        this.isSuccessful = false;
    }
}
