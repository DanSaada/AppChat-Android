package com.appchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.appchat.OperationCallback;
import com.appchat.R;
import com.appchat.api.UserApi;

public class LoginActivity extends AppCompatActivity implements OperationCallback {
    private boolean isSigninSuccessful;
    private UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.userApi = new UserApi();
        isSigninSuccessful = false;

        initUI();
    }

    private void initUI() {
        TextView signUpTextView = findViewById(R.id.signupTextView);
        signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {

            EditText usernameEditText = findViewById(R.id.usernameEditText);
            EditText passwordEditText = findViewById(R.id.passwordEditText);
            this.userApi.setCallback(this);
            this.userApi.checkTokenForLogin(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
            if (this.isSigninSuccessful) {
                Intent intent = new Intent(LoginActivity.this, ChatListActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Wrong username or password!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public void onSuccess() {
        this.isSigninSuccessful = true;
    }

    @Override
    public void onFail() {
        this.isSigninSuccessful = false;
    }
}