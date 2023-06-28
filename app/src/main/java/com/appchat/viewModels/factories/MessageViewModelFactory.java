package com.appchat.viewModels.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.appchat.viewModels.MessageViewModel;

public class MessageViewModelFactory implements ViewModelProvider.Factory {
    private String chatId;

    public MessageViewModelFactory(@NonNull String chatId) {
        this.chatId = chatId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MessageViewModel.class)) {
            return (T) new MessageViewModel(chatId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}

