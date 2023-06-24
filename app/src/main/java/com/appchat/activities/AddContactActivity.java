package com.appchat.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.appchat.R;

public class AddContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        ImageView closeBtn = findViewById(R.id.Exit);
        closeBtn.setOnClickListener(v -> finish());

        Button addContactBtn = findViewById(R.id.addButton);
        //TODO: Add contact to database and other logic related to that
        addContactBtn.setOnClickListener(v -> finish());
    }
}