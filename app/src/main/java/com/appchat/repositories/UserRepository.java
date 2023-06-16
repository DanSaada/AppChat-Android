package com.appchat.repositories;

import com.appchat.OperationCallback;
import com.appchat.api.UserApi;
import com.appchat.entities.User;

public class UserRepository {
    private UserApi userAPI;

    public UserRepository() {
        userAPI = new UserApi();
    }

    public User getUser(String userId) {
        return userAPI.getUser(userId);
    }

    public void setCallback(OperationCallback callback) {
        this.userAPI.setCallback(callback);
    }

    public void addUser(String username, String password, String nickname, byte[] picture) {
        userAPI.addUser(username, password, nickname, picture);
    }


}
