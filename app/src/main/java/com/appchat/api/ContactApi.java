package com.appchat.api;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.appchat.AppStateManager;
import com.appchat.OperationCallback;
import com.appchat.db.dao.ContactDao;
import com.appchat.entities.Contact;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
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

    public void getAllContacts(MutableLiveData<List<Contact>> contacts, String token) {
        Call<List<Contact>> call = webServiceApi.getAllContacts(token);
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(@NonNull Call<List<Contact>> call, @NonNull Response<List<Contact>> response) {
                new Thread(() -> {
                    contactDao.clear();
                    if (response.body() == null) {
                        return;
                    }

                    // add the all contacts to the local database
                    for (Contact contact : response.body()) {
                        contactDao.insert(contact);
                    }
                    contacts.postValue(contactDao.getAllContacts());
                }).start();
            }

            @Override
            public void onFailure(@NonNull Call<List<Contact>> call, @NonNull Throwable t) {
            }
        });
    }

    public void addContact(MutableLiveData<List<Contact>> contacts, Contact newContact, String token) {
        Call<Void> call = webServiceApi.postContact(newContact.getName(), token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                new Thread(() -> {
                    if (response.isSuccessful()) {
                        // Insert the new contact into the local database
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
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                // Handle failure case, if needed
                if(callback != null) {
                    callback.onFail();
                }
            }
        });
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
