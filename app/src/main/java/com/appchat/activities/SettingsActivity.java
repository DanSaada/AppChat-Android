package com.appchat.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.appchat.R;

public class SettingsActivity extends AppCompatActivity {

    private int previousUiMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button changeThemeButton = findViewById(R.id.changeThemeButton);
        changeThemeButton.setOnClickListener(v -> {
            int currentUiMode = getResources().getConfiguration().uiMode;
            if (currentUiMode != previousUiMode) {
                // The app theme has changed
                showToastMessage("App theme changed");
            }

            if (changeThemeButton.getText().equals("Dark Theme")) {
                changeThemeButton.setText("Light Theme");
                setAppTheme(R.style.AppThemeLight); // Apply light mode theme
            } else {
                changeThemeButton.setText("Dark Theme");
                setAppTheme(R.style.AppThemeDark); // Apply dark mode theme
            }
            previousUiMode = getResources().getConfiguration().uiMode;
            recreate(); // Recreate the activity to reflect the new theme
        });
    }

    private void setAppTheme(int themeResId) {
        getApplicationContext().setTheme(themeResId);
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
