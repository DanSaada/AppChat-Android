package com.appchat.api;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.appchat.Adapters.Json2EntityAdapter;
import com.appchat.AppStateManager;
import com.appchat.OperationCallback;
import com.appchat.db.dao.ContactDao;
import com.appchat.entities.Contact;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactApi {
    private ContactDao contactDao;
    private Retrofit retrofit;
    private WebServiceApi webServiceApi;
    private OperationCallback callback;

    public ContactApi(ContactDao contactDao) {
        // creates a Gson object
        Gson gson = new GsonBuilder().setLenient().create();
        // initialize the ContactDao object for to be able to do operations with the Room database
        this.contactDao = contactDao;
        // making the retrofit object to implement the web service API
        this.retrofit = new Retrofit.Builder()
                .baseUrl(AppStateManager.serverUrl + AppStateManager.serverPort + "/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        this.webServiceApi = this.retrofit.create(WebServiceApi.class);
    }

    public void setCallback(OperationCallback callback) {
        this.callback = callback;
    }

    //TODO: check that it works with List<JsonObject> instead of List<Contact>
    public void getAllContacts(MutableLiveData<List<Contact>> contacts, String token) {
        Call<List<JsonObject>> call = webServiceApi.getAllContacts(token);
        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(@NonNull Call<List<JsonObject>> call, @NonNull Response<List<JsonObject>> response) {
                new Thread(() -> {
                    contactDao.clear();
                    if (response.body() == null) {
                        return;
                    }

                    // new implementation:
                    List<Contact> contactList = Json2EntityAdapter.Json2ContactList(response.body());
                    for (Contact contact : contactList) {
                        String profilePicture = contact.getProfilePic();
                        if (profilePicture.startsWith("data:image/jpeg;base64,")) {
                            // Remove the prefix
                            profilePicture = profilePicture.substring("data:image/jpeg;base64,".length());
                            contact.setProfilePic(profilePicture);
                        }

                        contactDao.insert(contact);
                    }
                    contacts.postValue(contactDao.getAllContacts());
                }).start();
            }

            @Override
            public void onFailure(@NonNull Call<List<JsonObject>> call, @NonNull Throwable t) {
            }
        });
    }

    public void addContact(MutableLiveData<List<Contact>> contacts, String newContactName, String token) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("username", newContactName);

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody.toString());
            Call<JsonObject> call = webServiceApi.postContact(body, token);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                    new Thread(() -> {

                        if (response.isSuccessful()) {
                            JsonObject responseBody = response.body();
                            if (responseBody == null) {
                                callback.onFail();
                                return;
                            }

                            // Create a new contact object with the id and name of the new contact
                            Contact newContact = Json2EntityAdapter.JsonToContact(responseBody);
                            String profilePicture = newContact.getProfilePic();
                            if (profilePicture.startsWith("data:image/jpeg;base64,")) {
                                // Remove the prefix
                                profilePicture = profilePicture.substring("data:image/jpeg;base64,".length());
                                newContact.setProfilePic(profilePicture);
                            }
                            contactDao.insert(newContact);

                            // Update the MutableLiveData with the updated list of contacts
                            contacts.postValue(contactDao.getAllContacts());
                            callback.onSuccess();
                        } else if(callback != null) {
                            callback.onFail();
                        }
                    }).start();
                }

                @Override
                public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                    // Handle failure case, if needed
                    if(callback != null) {
                        callback.onFail();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFail();
        }
    }

    public void getContact(MutableLiveData<Contact> contactLiveData, String id, String token) {
        Call<Contact> call = webServiceApi.getContact(id, token);
        call.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(@NonNull Call<Contact> call, @NonNull Response<Contact> response) {
                new Thread(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        // Successfully retrieved the contact from the server
                        // Update the MutableLiveData with the fetched contact

                        Contact contact = response.body();
                        String profilePicture = contact.getProfilePic();
                        if (profilePicture.startsWith("data:image/jpeg;base64,")) {
                            // Remove the prefix
                            profilePicture = profilePicture.substring("data:image/jpeg;base64,".length());
                            contact.setProfilePic(profilePicture);
                        }

                        // Update the MutableLiveData with the fetched contact
                        contactLiveData.postValue(contact);
                    }
                }).start();
            }

            @Override
            public void onFailure(@NonNull Call<Contact> call, @NonNull Throwable t) {
                // Handle failure case, if needed
            }
        });
    }

    public void deleteContact(MutableLiveData<List<Contact>> contacts, String id, String token) {
        Call<Void> call = webServiceApi.deleteContact(id, token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                new Thread(() -> {
                    if (response.isSuccessful()) {
                        // Successfully deleted the contact from the server
                        // Retrieve the contact from the local database
                        Contact contactToDelete = contactDao.get(id);
                        if (contactToDelete != null) {
                            // Delete the contact from the local database
                            contactDao.delete(contactToDelete);

                            // Update the MutableLiveData with the updated contact list from the database
                            contacts.postValue(contactDao.getAllContacts());
                        }
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
