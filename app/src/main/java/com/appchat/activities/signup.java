package com.appchat.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appchat.R;

public class signup extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                // Get the URI of the selected image
                Uri imageUri = data.getData();

                // Use the selected image URI as needed (e.g., display it in an ImageView)
                Button uploadPictureButton = findViewById(R.id.uploadPictureButton);
                uploadPictureButton.setImageURI(imageUri);
            }
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_TITLE , "Select Image");
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextView signUpTextView = findViewById(R.id.signInTextView);
        signUpTextView.setOnClickListener(v -> {
            finish();
        });

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> {
            openFileChooser();
        });
    }
}