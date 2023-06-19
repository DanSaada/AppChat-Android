package com.appchat.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appchat.R;
import com.appchat.activities.signupServices.SignupErrors;
import com.appchat.activities.signupServices.ValidateInputsService;
import com.google.android.material.textfield.TextInputLayout;


import java.io.IOException;

public class SignupActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private Button uploadPictureBtn;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    // EditText fields
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText displayNameEditText;

    // TextInputLayouts
    private TextInputLayout usernameTextInputLayout;
    private TextInputLayout passwordTextInputLayout;
    private TextInputLayout confirmPasswordTextInputLayout;
    private TextInputLayout displayNameTextInputLayout;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextView signUpTextView = findViewById(R.id.signInTextView);
        signUpTextView.setOnClickListener(v -> finish());

        profileImageView = findViewById(R.id.profileImageView);
        uploadPictureBtn = findViewById(R.id.uploadPictureButton);

        // Initialize EditText fields
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        displayNameEditText = findViewById(R.id.displayNameEditText);

        // Initialize TextInputLayouts
        usernameTextInputLayout = findViewById(R.id.usernameTextInputLayout);
        passwordTextInputLayout = findViewById(R.id.passwordTextInputLayout);
        confirmPasswordTextInputLayout = findViewById(R.id.confirmPasswordTextInputLayout);
        displayNameTextInputLayout = findViewById(R.id.displayNameTextInputLayout);

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Uri imageUri = data.getData(); // retrieve the URI of the selected image
                    handleImageSelection(imageUri);
                }
            }
        });

        uploadPictureBtn.setOnClickListener(v -> {
            uploadPictureBtn.setText("Change Picture");
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        // TODO: Add validation for all fields
        Button registerBtn = findViewById(R.id.registerButton);
        registerBtn.setOnClickListener(v -> {
            boolean flag = true;
            // Save the input from each field
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();
            String displayName = displayNameEditText.getText().toString();

            // Clear any existing error messages
            usernameTextInputLayout.setError(null);
            passwordTextInputLayout.setError(null);
            confirmPasswordTextInputLayout.setError(null);

            // Perform validation and show error messages if necessary
                SignupErrors usernameError = ValidateInputsService.validateUsername(username);
                if (usernameError != SignupErrors.OK || username.isEmpty()) {
                    usernameTextInputLayout.setError("Invalid username");
                    usernameTextInputLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
                    flag = false;
                }
                SignupErrors passwordError = ValidateInputsService.validatePassword(password);
                if (passwordError != SignupErrors.OK || password.isEmpty()) {
                    passwordTextInputLayout.setError(getString(R.string.password_required_error));
                    passwordTextInputLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
                    flag = false;
            }
                SignupErrors confirmPasswordError = ValidateInputsService.
                        validateConfirmPassword(password, confirmPassword);
                if (confirmPasswordError != SignupErrors.OK || confirmPassword.isEmpty()) {
                    confirmPasswordTextInputLayout.setError(getString(R.string.password_mismatch_error));
                    confirmPasswordTextInputLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
                    flag = false;
                }
                SignupErrors displayNameError = ValidateInputsService.validateDisplayName(displayName);
                if (displayNameError != SignupErrors.OK || displayName.isEmpty()) {
                    displayNameTextInputLayout.setError(getString(R.string.display_name_required_error));
                    displayNameTextInputLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
                    flag = false;
                }
            if (flag) {
                finish();
            }
        });
    }

    private void handleImageSelection(Uri imageUri) {
        try {
            Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

            int desiredWidth = 50;
            int desiredHeight = 50;

            Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, desiredWidth, desiredHeight, true);

            // Rotate the image if necessary
            Matrix matrix = new Matrix();
            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

            profileImageView.setImageBitmap(rotatedBitmap);
            profileImageView.setVisibility(View.VISIBLE); // Set the visibility of profileImageView to visible
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
