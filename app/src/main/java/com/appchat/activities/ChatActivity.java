package com.appchat.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appchat.Adapters.ChatMessagesListAdapter;
import com.appchat.AppStateManager;
import com.appchat.R;
import com.appchat.entities.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // for debug
        AppStateManager.loggedUser = "1";

        RecyclerView chatMessagesList = findViewById(R.id.chatRecyclerView);
        final ChatMessagesListAdapter adapter = new ChatMessagesListAdapter(this);
        chatMessagesList.setAdapter(adapter);
        chatMessagesList.setLayoutManager(new LinearLayoutManager(this));

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("wassap", "21:57", true, "1", "2"));
        messages.add(new Message("good", "21:58", true, "2", "1"));
        messages.add(new Message("hii", "21:59", true, "1", "2"));
        messages.add(new Message("wiii", "22:00", true, "2", "1"));
        messages.add(new Message("AMEN ON THE ONE!!!", "22:01", true, "1", "2"));
        messages.add(new Message("PROBABLY NOT!!!", "22:02", true, "2", "1"));
        adapter.setMessages(messages);

    }
}