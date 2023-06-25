package com.appchat.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appchat.Adapters.ContactListAdapter;
import com.appchat.R;
import com.appchat.viewModels.ContactsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ChatListActivity extends AppCompatActivity {

    private ContactsViewModel contactsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);

        RecyclerView contactList = findViewById(R.id.chatListRecyclerView);
        final ContactListAdapter adapter = new ContactListAdapter(this);
        contactList.setAdapter(adapter);
        contactList.setLayoutManager(new LinearLayoutManager(this));


        adapter.setContacts(contactsViewModel.getContacts().getValue());

        FloatingActionButton fabAddChat = findViewById(R.id.fabAddChat);
        // TODO: change addContactFragment into Activity
        fabAddChat.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatListActivity.this);
            final View customLayout = getLayoutInflater().inflate(R.layout.fragment_add_contact, null);
            builder.setView(customLayout);
            AlertDialog dialog = builder.create();
            ImageButton exitBtn = customLayout.findViewById(R.id.Exit);
            exitBtn.setOnClickListener(v1 -> dialog.dismiss());
            Button addContactBtn = customLayout.findViewById(R.id.addButton);
            addContactBtn.setOnClickListener(v1 -> {
                EditText usernameEditText = customLayout.findViewById(R.id.usernameEditText);
                contactsViewModel.createChat(usernameEditText.getText().toString());
                dialog.dismiss();
        });
        dialog.show();
        });
    }
}