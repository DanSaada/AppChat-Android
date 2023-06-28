package com.appchat.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Ignore;

import com.appchat.entities.Message;
import com.appchat.repositories.MessageRepository;

import java.util.List;

public class MessageViewModel extends ViewModel {
    private MessageRepository messageRepository;
    private LiveData<List<Message>> messages;

    private String chatID;

    @Ignore
    public MessageViewModel() {
        // No-arg constructor
    }
    public MessageViewModel(String chatID) {
        this.chatID = chatID;
        this.messageRepository = new MessageRepository(chatID);
        this.messages = messageRepository.get();
    }

    public LiveData<List<Message>> get() {
        return messages;
    }


    public void add(String content) {
        messageRepository.add(chatID, content);
    }

    public void reload() {
        messageRepository.reload();
    }

    public void refresh(String chatID) {
        messageRepository.setMessagesOfLocalDatabaseWithServerApiMessages(chatID);
    }
}
