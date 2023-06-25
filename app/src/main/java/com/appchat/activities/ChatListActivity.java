package com.appchat.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appchat.Adapters.ContactListAdapter;
import com.appchat.OperationCallback;
import com.appchat.R;
import com.appchat.entities.Contact;
import com.appchat.viewModels.ContactsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ChatListActivity extends AppCompatActivity implements OperationCallback {

    private ContactsViewModel contactsViewModel;
    private AlertDialog dialog;

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
            final View customLayout = getLayoutInflater().inflate(R.layout.activity_add_contact, null);
            builder.setView(customLayout);
            dialog = builder.create();
            ImageButton exitBtn = customLayout.findViewById(R.id.Exit);
            exitBtn.setOnClickListener(v1 -> dialog.dismiss());
            Button addContactBtn = customLayout.findViewById(R.id.addButton);
            addContactBtn.setOnClickListener(v1 -> {
                EditText usernameEditText = customLayout.findViewById(R.id.usernameEditText);
                Contact contact = contactsViewModel.get(usernameEditText.getText().toString()).getValue();
                if (contact != null) {
                    contactsViewModel.setCallback(this);
                    contactsViewModel.add(contact);
                }
        });
        dialog.show();
        });
    }

    @Override
    public void onSuccess() {
        dialog.dismiss();
    }

    @Override
    public void onFail() {
        Toast.makeText(this, "Wrong Username", Toast.LENGTH_SHORT).show();
    }
}