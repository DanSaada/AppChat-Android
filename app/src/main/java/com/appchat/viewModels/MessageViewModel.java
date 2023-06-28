package com.appchat.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.appchat.entities.Message;
import com.appchat.repositories.MessageRepository;
import java.util.List;

public class MessageViewModel extends ViewModel {
    private MessageRepository messageRepository;
    private LiveData<List<Message>> messages;

    public MessageViewModel() {
        this.messageRepository = new MessageRepository();
        this.messages = messageRepository.get();
    }

    public LiveData<List<Message>> get() {
        return messages;
    }

    public void add(String message) {
        messageRepository.add(message);
    }

    public void reload() {
        messageRepository.reload();
    }

    public void refresh() {
        messageRepository.setMessagesOfLocalDatabaseWithServerApiMessages();
    }
}
