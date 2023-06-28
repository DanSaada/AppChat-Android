package com.appchat.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SigninViewModel extends ViewModel {
    private MutableLiveData<String> username;
    private MutableLiveData<String> password;


    public MutableLiveData<String> getUsername() {
        if (username == null) {
            username = new MutableLiveData<String>();
        }
        return username;
    }

    public MutableLiveData<String> getPassword() {
        if (password == null) {
            password = new MutableLiveData<String>();
        }
        return password;
    }
}
