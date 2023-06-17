package com.appchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
        });
    }
}