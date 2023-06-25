package com.appchat.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appchat.Adapters.ChatMessagesListAdapter;
import com.appchat.R;
import com.appchat.viewModels.MessageViewModel;

public class ChatActivity extends AppCompatActivity {

    private MessageViewModel messageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);


        RecyclerView chatMessagesList = findViewById(R.id.chatRecyclerView);
        final ChatMessagesListAdapter adapter = new ChatMessagesListAdapter(this);
        chatMessagesList.setAdapter(adapter);
        chatMessagesList.setLayoutManager(new LinearLayoutManager(this));

        adapter.setMessages(messageViewModel.get().getValue());

    }
}