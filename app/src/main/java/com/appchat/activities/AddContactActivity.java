package com.appchat.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.appchat.R;
import com.appchat.entities.Contact;
import com.appchat.viewModels.ContactsViewModel;

public class AddContactActivity extends AppCompatActivity {

    private ContactsViewModel contactsViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        ImageView closeBtn = findViewById(R.id.Exit);
        closeBtn.setOnClickListener(v -> finish());

        // Retrieve the ContactsViewModel object
        contactsViewModel = (ContactsViewModel) getIntent().getSerializableExtra("contactsViewModel");

        Button addContactBtn = findViewById(R.id.addButton);
        addContactBtn.setOnClickListener(v -> {
            EditText usernameEditText = findViewById(R.id.usernameEditText);
            addContact(usernameEditText.getText().toString());
        });
    }

    private void addContact(String username) {
        Contact contact = contactsViewModel.get(username).getValue();
        if (contact != null) {
            contactsViewModel.add(contact);
            finish();
        } else {
            Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show();
        }
    }
}