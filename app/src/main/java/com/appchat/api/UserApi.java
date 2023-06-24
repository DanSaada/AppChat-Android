package com.appchat.api;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.appchat.AppStateManager;
import com.appchat.OperationCallback;
import com.appchat.db.dao.UserDao;
import com.appchat.entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApi {
    private UserDao userDao;
    Retrofit retrofit;
    WebServiceApi webServiceApi;
    OperationCallback callback;

    public UserApi() {
        // creates a Gson object
        Gson gson = new GsonBuilder().setLenient().create();
        // initialize the userDao object for to be able to do operations with the Room database
        userDao = AppStateManager.usersDB.userDao();
        // making the retrofit object to implement the web service API
        retrofit = new Retrofit.Builder()
                .baseUrl(AppStateManager.serverUrl + AppStateManager.serverPort + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceApi = retrofit.create(WebServiceApi.class);
    }

    public void setCallback(OperationCallback callback) {
        this.callback = callback;
    }

    public void checkTokenForLogin(String username, String password) {
        User user = new User(username, password);
        Call<JsonPrimitive> call = webServiceApi.createToken(user);
        // start the async network request and attache a callback to handle the response
        call.enqueue(new Callback<JsonPrimitive>() {
            // response is received from the server
            @Override
            public void onResponse(@NonNull Call<JsonPrimitive> call, @NonNull Response<JsonPrimitive> response) {
                if (response.isSuccessful() && response.code() == 200 && response.body() != null) {
                    // extract the logged in user's token
                    String token = response.body().toString();
                    AppStateManager.loggerUserToken = token.substring(1, token.length() - 1);
                    callback.onSuccess();
                } else {
                    callback.onFail();
                }
            }
            // failure in the network request.
            @Override
            public void onFailure(@NonNull Call<JsonPrimitive> call, @NonNull Throwable t) {
                callback.onFail();
            }
        });
    }

    public void addUser(String username, String password, String displayName, String profilePic) {
        // create the new User object
        User user = new User(username, password, displayName, profilePic);
        Call<JsonObject> call = webServiceApi.postUser(user);
        // start the async network request and attache a callback to handle the response
        call.enqueue(new Callback<JsonObject>() {
            // response is received from the server
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful() && response.code() == 200 && response.body() != null) {
                    // add new user to the room database

                    // debug
                    System.out.println("UserApi.addUser(): " + response.body().toString());
                    // end debug
                    userDao.insert(user);
                    callback.onSuccess();
                } else {

                    // debug
                    System.out.println("UserApi.addUser(): failed -  " + response.body().toString());
                    // end debug
                    callback.onFail();
                }
            }
            // failure in the network request.
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                // debug
                System.out.println("UserApi.addUser(): failed - from onFailure() " + t.getMessage());
                // end debug
                callback.onFail();
            }
        });
    }

    public User getUser(String userId) {
        RoomUsers roomUsers = new RoomUsers(userDao, userId);
        User user;
        try {
            // executes the RoomUsers task asynchronously
            user = roomUsers.execute().get();
        }
        catch (Exception exception) {
            user = null;
        }
        return user;
    }

    // get user from room database async
    private class RoomUsers extends AsyncTask<Void, Void, User> {
        private UserDao userDao;
        private String userId;

        public RoomUsers(UserDao userDao, String userId) {
            this.userDao = userDao;
            this.userId = userId;
        }

        @Override
        protected User doInBackground(Void... voids) {
            return userDao.get(userId);
        }
    }
}
