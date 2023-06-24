package com.appchat.viewModels;

import androidx.lifecycle.ViewModel;
import com.appchat.OperationCallback;
import com.appchat.entities.User;
import com.appchat.repositories.UserRepository;

public class UserViewModel extends ViewModel {
    private UserRepository userRepository;

    public UserViewModel() {
        this.userRepository = new UserRepository();
    }

    public User getUser(String userId) {
        return userRepository.getUser(userId);
    }

    public void setCallback(OperationCallback callback) {
        this.userRepository.setCallback(callback);
    }

    public void registerUser(String username, String password, String displayName, String picture) {
        userRepository.addUser(username, password, displayName, picture);
    }
}
