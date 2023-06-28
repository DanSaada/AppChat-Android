package com.appchat;

import androidx.lifecycle.MutableLiveData;

import com.appchat.entities.Message;

public class SingletonFirebase {
    private static MutableLiveData<String> contactFirebase;
    private static MutableLiveData<Message> messageFirebase;

    private SingletonFirebase() {
        // Private constructor to prevent instantiation
    }

    public static synchronized MutableLiveData<String> getFirebaseContactInstance() {
        if (contactFirebase == null) {
            contactFirebase = new MutableLiveData<>();
        }
        return contactFirebase;
    }



    public static synchronized MutableLiveData<Message> getFirebaseMessageInstance() {
        if (messageFirebase == null) {
            messageFirebase =  new MutableLiveData<>();
        }
        return messageFirebase;
    }
}
