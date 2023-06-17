package com.appchat.api;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.appchat.AppStateManager;
import com.appchat.db.dao.MessageDao;
import com.appchat.entities.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessageApi {
    private MessageDao messageDao;
    private Retrofit retrofit;
    private WebServiceApi webServiceApi;

    public MessageApi(MessageDao messageDao) {
        // creates a Gson object
        Gson gson = new GsonBuilder().setLenient().create();
        // initialize the MessageDao object for to be able to do operations with the Room database
        this.messageDao = messageDao;
        // making the retrofit object to implement the web service API
        this.retrofit = new Retrofit.Builder()
                .baseUrl(AppStateManager.serverUrl + AppStateManager.serverPort + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.webServiceApi = this.retrofit.create(WebServiceApi.class);
    }

    public void getAllMessages(MutableLiveData<List<Message>> messages, String token, String contactId) {
        Call<List<Message>> call = webServiceApi.getMessages(contactId, token);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(@NonNull Call<List<Message>> call,
                                   @NonNull Response<List<Message>> response) {
                new Thread(() -> {
                    messageDao.clear();
                    if (response.body() == null) {
                        return;
                    }

                    // add the all messages to the dao
                    for (Message message : response.body()) {
                        messageDao.insert(message);
                    }
                    messages.postValue(response.body());
                }).start();
            }

            @Override
            public void onFailure(@NonNull Call<List<Message>> call, @NonNull Throwable t) {
            }
        });
    }

    public void addMessage(MutableLiveData<List<Message>> messages, String token, String contactId, String message) {
        Call<Void> call = webServiceApi.postMessage(contactId, message, token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                new Thread(() -> {
                    if (response.isSuccessful()) {
                        // Successfully added the message to the server

                        // Insert the new message into the local database in the relevant chat
                        Message newMessage = new Message(message, new Date().toString(), true, AppStateManager.loggedUser, contactId);
                        messageDao.insert(newMessage);

                        // Update the MutableLiveData with the updated list of messages
                        messages.postValue(messageDao.getChatMessages(AppStateManager.loggedUser, contactId));
                    }
                }).start();
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                // Handle failure case, if needed
            }
        });
    }
}
