package com.appchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appchat.Adapters.ContactListAdapter;
import com.appchat.OperationCallback;
import com.appchat.R;
import com.appchat.SingletonFirebase;
import com.appchat.entities.Message;
import com.appchat.viewModels.ContactsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class ChatListActivity extends AppCompatActivity implements OperationCallback {

    private ContactsViewModel contactsViewModel;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        MutableLiveData<String> contactsFirebase = SingletonFirebase.getFirebaseContactInstance();
        MutableLiveData<Message> messagesFirebase = SingletonFirebase.getFirebaseMessageInstance();

        contactsViewModel = new ViewModelProvider(this).get(ContactsViewModel.class);

        RecyclerView contactList = findViewById(R.id.chatListRecyclerView);
        final ContactListAdapter adapter = new ContactListAdapter(this);
        contactList.setAdapter(adapter);
        contactList.setLayoutManager(new LinearLayoutManager(this));

        contactsViewModel.getContacts().observe(this, contacts -> {
            // Update the cached copy of the words in the adapter.
            adapter.setContacts(contacts);
            adapter.notifyDataSetChanged();
        });
        adapter.setContacts(contactsViewModel.getContacts().getValue());

        //listen to view model changes.
        contactsFirebase.observe(this, contacts -> {
            contactsViewModel.reload();
        });

        messagesFirebase.observe(this, messages -> {
            contactsViewModel.reload();
        });

        FloatingActionButton fabAddChat = findViewById(R.id.fabAddChat);
        fabAddChat.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ChatListActivity.this);
            final View customLayout = getLayoutInflater().inflate(R.layout.activity_add_contact, null);
            builder.setView(customLayout);
            dialog = builder.create();
            ImageView exitBtn = customLayout.findViewById(R.id.Exit);
            exitBtn.setOnClickListener(v1 -> dialog.dismiss());
            Button addContactBtn = customLayout.findViewById(R.id.addButton);
            addContactBtn.setOnClickListener(v1 -> {
                EditText usernameEditText = customLayout.findViewById(R.id.usernameEditText);
                String input = usernameEditText.getText().toString();
                contactsViewModel.setCallback(this);
                contactsViewModel.add(input);
        });
        dialog.show();
        });

        ImageView settingsBtn = findViewById(R.id.settingsButton);
        settingsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ChatListActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onSuccess() {
        runOnUiThread(() -> {
            Toast.makeText(ChatListActivity.this, "Contact added successfully",
                    Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }


    @Override
    public void onFail() {
        runOnUiThread(() -> Toast.makeText(ChatListActivity.this, "Something Went Wrong, Please Try Again",
                Toast.LENGTH_SHORT).show());
    }
}