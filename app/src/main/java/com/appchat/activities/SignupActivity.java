package com.appchat.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.appchat.OperationCallback;
import com.appchat.R;
import com.appchat.activities.signupServices.SignupErrors;
import com.appchat.activities.signupServices.ValidateInputsService;
import com.appchat.viewModels.SignupViewModel;
import com.appchat.viewModels.UserViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;

public class SignupActivity extends AppCompatActivity implements OperationCallback {

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

    private UserViewModel userViewModel;
    private SignupViewModel signupViewModel;
    private boolean isSignupSuccessful;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        TextView signUpTextView = findViewById(R.id.signInTextView);
        signUpTextView.setOnClickListener(v -> finish());


        init();

        initViewModelLogic();

        onChangeUsername();

        onChangePassword();

        onChangeConfirmPassword();

        onChangeDisplayName();

        initUploadPictureBtn();

        initSettingsBtn();

        initInputsValidation();
    }

    private void initViewModelLogic() {
        signupViewModel = new ViewModelProvider(this).get(SignupViewModel.class);

        // Observe the change in the input fields
        signupViewModel.getUsername().observe(this, s -> {
            if (s != null) {
                SignupErrors usernameError = ValidateInputsService.validateUsername(s);
                signupViewModel.getUsernameError().setValue(usernameError);
            }
        });

        signupViewModel.getPassword().observe(this, s -> {
            if (s != null) {
                SignupErrors passwordError = ValidateInputsService.validatePassword(s);
                signupViewModel.getPasswordError().setValue(passwordError);
            }
        });

        signupViewModel.getConfirmPassword().observe(this, s -> {
            if (s != null) {
                String password = signupViewModel.getPassword().getValue();
                SignupErrors confirmPasswordError =
                        ValidateInputsService.validateConfirmPassword(password, s);
                signupViewModel.getConfirmPasswordError().setValue(confirmPasswordError);
            }
        });

        signupViewModel.getDisplayName().observe(this, s -> {
            if (s != null) {
                SignupErrors displayNameError = ValidateInputsService.validateDisplayName(s);
                signupViewModel.getDisplayNameError().setValue(displayNameError);
            }
        });

        signupViewModel.getUsernameError().observe(this, this::checkUsernameError);

        signupViewModel.getPasswordError().observe(this, this::checkPasswordError);

        signupViewModel.getConfirmPasswordError().observe(this, this::checkConfirmPasswordError);

        signupViewModel.getDisplayNameError().observe(this, this::checkDisplayNameError);
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
            signupViewModel.getProfilePicError().setValue(SignupErrors.OK);
        } catch (IOException e) {
            //TODO: check if this line is necessary, might not be needed
            e.printStackTrace();
            signupViewModel.getProfilePicError().setValue(SignupErrors.INVALID_PROFILE_PIC);
        }
    }

    private void initSettingsBtn() {
        ImageButton settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void init() {

        isSignupSuccessful = false;
        this.userViewModel = new UserViewModel();

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

    }

    private void onChangeUsername() {
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signupViewModel.getUsername().setValue(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }
        });
    }

    private void onChangePassword() {
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signupViewModel.getPassword().setValue(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }
        });
    }

    private void onChangeConfirmPassword() {
        confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signupViewModel.getConfirmPassword().setValue(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }
        });
    }

    private void onChangeDisplayName() {
        displayNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                signupViewModel.getDisplayName().setValue(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // do nothing
            }
        });
    }

    private void initUploadPictureBtn() {

        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    Uri imageUri = data.getData(); // retrieve the URI of the selected image
                    handleImageSelection(imageUri);
                } else {
                    signupViewModel.getProfilePicError().setValue(SignupErrors.INVALID_PROFILE_PIC);
                }
            }
        });

        uploadPictureBtn.setOnClickListener(v -> {
            uploadPictureBtn.setText("Change Picture");
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });
    }

    private void inputsValidation() {

        // Clear any existing error messages
        usernameTextInputLayout.setError(null);
        passwordTextInputLayout.setError(null);
        confirmPasswordTextInputLayout.setError(null);
        displayNameTextInputLayout.setError(null);

        // new implementation:
        SignupErrors usernameError = this.signupViewModel.getUsernameError().getValue();
        SignupErrors passwordError = this.signupViewModel.getPasswordError().getValue();
        SignupErrors confirmPasswordError = this.signupViewModel.getConfirmPasswordError().getValue();
        SignupErrors displayNameError = this.signupViewModel.getDisplayNameError().getValue();
        SignupErrors profilePicError = this.signupViewModel.getProfilePicError().getValue();

        boolean isValidationSuccessful = (usernameError == SignupErrors.OK) &&
                (passwordError == SignupErrors.OK) && (confirmPasswordError == SignupErrors.OK) &&
                (displayNameError == SignupErrors.OK) && (profilePicError == SignupErrors.OK);

        if (isValidationSuccessful) {
            this.userViewModel.setCallback(this);

            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String displayName = displayNameEditText.getText().toString();
//            byte[] profileImage = Base64TypeConverter.fromBase64String(profileImageView.toString());
            this.userViewModel.registerUser(username, password, displayName, profileImageView.toString());
            if (isSignupSuccessful) {
                Toast.makeText(this, "Signup successful", Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                Toast.makeText(this, "Signup failed", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Please Fill All Fields Correctly", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUsernameError(SignupErrors usernameError) {
        if (usernameError != SignupErrors.OK ) {
            usernameTextInputLayout.setError(getString(R.string.username_required_error));
            usernameTextInputLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
        } else {
            usernameTextInputLayout.setError(null);
        }
    }

    private void checkPasswordError(SignupErrors passwordError) {
        if (passwordError != SignupErrors.OK ) {
            passwordTextInputLayout.setError(getString(R.string.password_required_error));
            passwordTextInputLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
        } else {
            passwordTextInputLayout.setError(null);
        }
    }

    private void checkConfirmPasswordError(SignupErrors confirmPasswordError) {
        if (confirmPasswordError != SignupErrors.OK ) {
            confirmPasswordTextInputLayout.setError(getString(R.string.password_mismatch_error));
            confirmPasswordTextInputLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
        } else {
            confirmPasswordTextInputLayout.setError(null);
        }
    }

    private void checkDisplayNameError(SignupErrors displayNameError) {
        if (displayNameError != SignupErrors.OK ) {
            displayNameTextInputLayout.setError(getString(R.string.display_name_required_error));
            displayNameTextInputLayout.setErrorTextColor(ColorStateList.valueOf(Color.RED));
        } else {
            displayNameTextInputLayout.setError(null);
        }
    }

    private void initInputsValidation() {
        Button registerBtn = findViewById(R.id.registerButton);
        registerBtn.setOnClickListener(v -> {
            inputsValidation();
        });
    }

    @Override
    public void onSuccess() {
        this.isSignupSuccessful = true;
    }

    @Override
    public void onFail() {
        this.isSignupSuccessful = false;
    }
}
