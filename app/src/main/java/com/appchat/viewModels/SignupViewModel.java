package com.appchat.viewModels;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.appchat.activities.signupServices.SignupErrors;

public class SignupViewModel extends ViewModel {
    private MutableLiveData<String> username;
    private MutableLiveData<String> password;
    private MutableLiveData<String> confirmPassword;
    private MutableLiveData<String> displayName;

    private MutableLiveData<SignupErrors> usernameError;
    private MutableLiveData<SignupErrors> passwordError;
    private MutableLiveData<SignupErrors> confirmPasswordError;
    private MutableLiveData<SignupErrors> displayNameError;


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

    public MutableLiveData<String> getConfirmPassword() {
        if (confirmPassword == null) {
            confirmPassword = new MutableLiveData<String>();
        }
        return confirmPassword;
    }

    public MutableLiveData<String> getDisplayName() {
        if (displayName == null) {
            displayName = new MutableLiveData<String>();
        }
        return displayName;
    }


    public MutableLiveData<SignupErrors> getUsernameError() {
        if (usernameError == null) {
            usernameError = new MutableLiveData<SignupErrors>();
        }
        return usernameError;
    }

    public MutableLiveData<SignupErrors> getPasswordError() {
        if (passwordError == null) {
            passwordError = new MutableLiveData<SignupErrors>();
        }
        return passwordError;
    }

    public MutableLiveData<SignupErrors> getConfirmPasswordError() {
        if (confirmPasswordError == null) {
            confirmPasswordError = new MutableLiveData<SignupErrors>();
        }
        return confirmPasswordError;
    }

    public MutableLiveData<SignupErrors> getDisplayNameError() {
        if (displayNameError == null) {
            displayNameError = new MutableLiveData<SignupErrors>();
        }
        return displayNameError;
    }
}
