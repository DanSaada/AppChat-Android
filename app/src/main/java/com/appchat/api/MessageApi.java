package com.appchat.api;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.appchat.AppStateManager;
import com.appchat.db.dao.MessageDao;
import com.appchat.entities.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
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

    public void getAllMessages(MutableLiveData<List<Message>> messages, String token, String chatID) {
        // FIXME: fix the call to the web service to get teh right parameters and convert correctly the response
        Call<List<JsonObject>> call = webServiceApi.getMessages(chatID, token);
        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(@NonNull Call<List<JsonObject>> call,
                                   @NonNull Response<List<JsonObject>> response) {
                new Thread(() -> {
                    messageDao.clear();
                    if (response.body() == null) {
                        return;
                    }

//                    // add the all messages to the dao
//                    for (Message message : response.body()) {
//                        messageDao.insert(message);
//                    }
//                    messages.postValue(response.body());
                }).start();
            }

            @Override
            public void onFailure(@NonNull Call<List<JsonObject>> call, @NonNull Throwable t) {
            }
        });
    }

    //TODO: i need to pass the chatID because the sender and receiver are known from the token and AppStateManager
    public void addMessage(MutableLiveData<List<Message>> messages, String token, String chatID, String content) {
        try {

            JSONObject requestBody = new JSONObject();
            requestBody.put("msg", content);
            RequestBody body = RequestBody.create(MediaType.parse("application/json"),
                    requestBody.toString());
            Call<JsonObject> call = webServiceApi.postMessage(chatID, body,
                    token);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                    new Thread(() -> {
                        if (response.isSuccessful() && response.body() != null) {
                            // Successfully added the message to the server

                            // Insert the new message into the local database in the relevant chat
                            Message newMessage = new Message(content, new Date().toString(),
                                    true, AppStateManager.loggedUser,
                                    AppStateManager.contactId);
                            messageDao.insert(newMessage);

                            // Update the MutableLiveData with the updated list of messages
                            messages.postValue(messageDao.getChatMessages(AppStateManager.loggedUser, AppStateManager.contactId));
                        }
                    }).start();
                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    // Handle failure case, if needed
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: think about adding here callback onFail
        }

    }
}
