package com.appchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.appchat.R;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView signUpTextView = findViewById(R.id.signupTextView);
        signUpTextView.setOnClickListener(v -> {
           Intent intent = new Intent(login.this, signup.class);
              startActivity(intent);
        });

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
             //TODO: make sure that the username and password are correct and then go to the chat list activity
            //Otherwise, show an error message and stay in the current activity
            Intent intent = new Intent(login.this, ChatListActivity.class);
            startActivity(intent);
        });
    }
}