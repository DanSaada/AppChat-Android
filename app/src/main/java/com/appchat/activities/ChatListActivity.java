package com.appchat.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appchat.Adapters.ContactListAdapter;
import com.appchat.R;
import com.appchat.entities.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ChatListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        RecyclerView contactList = findViewById(R.id.chatListRecyclerView);
        final ContactListAdapter adapter = new ContactListAdapter(this);
        contactList.setAdapter(adapter);
        contactList.setLayoutManager(new LinearLayoutManager(this));

        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("1", "name", "lastMsg", "sentTime", "1", 0, R.drawable.cat));
        contacts.add(new Contact("2", "name1", "lastMsg", "sentTime", "2", 0, R.drawable.cat));
        contacts.add(new Contact("3", "name2", "lastMsg", "sentTime", "3", 0, R.drawable.cat));
        contacts.add(new Contact("4", "name3", "lastMsg", "sentTime", "4", 0, R.drawable.cat));
        contacts.add(new Contact("5", "name4", "lastMsg", "sentTime", "5", 0, R.drawable.cat));
        contacts.add(new Contact("6", "name5", "lastMsg", "sentTime", "6", 0, R.drawable.cat));
        contacts.add(new Contact("7", "name6", "lastMsg", "sentTime", "7", 0, R.drawable.cat));
        contacts.add(new Contact("8", "name7", "lastMsg", "sentTime", "8", 0, R.drawable.cat));
        contacts.add(new Contact("9", "name8", "lastMsg", "sentTime", "9", 0, R.drawable.cat));
        adapter.setContacts(contacts);

        FloatingActionButton fabAddChat = findViewById(R.id.fabAddChat);
        // TODO: change addContactFragment into Activity
        fabAddChat.setOnClickListener(v -> {
            Intent intent = new Intent(ChatListActivity.this, AddContactFragment.class);
            startActivity(intent);
        });

    }
}