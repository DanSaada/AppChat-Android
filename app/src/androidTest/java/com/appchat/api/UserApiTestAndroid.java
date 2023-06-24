package com.appchat.api;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.appchat.AppStateManager;
import com.appchat.entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RunWith(AndroidJUnit4.class)
public class UserApiTestAndroid {

    private WebServiceApi webServiceApi;

    @Before
    public void setup() {
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
    public void setCallback() {
    }

    @Test
    public void checkTokenForLogin() {
    }

    @Test
    public void addUser() {
    }

    @Test
    public void getUser() {
    }
}
