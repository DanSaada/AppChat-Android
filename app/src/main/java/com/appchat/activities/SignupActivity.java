package com.appchat.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.appchat.R;

public class SignupActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextView signUpTextView = findViewById(R.id.signInTextView);
        signUpTextView.setOnClickListener(v -> {
            finish();
        });
    }


}