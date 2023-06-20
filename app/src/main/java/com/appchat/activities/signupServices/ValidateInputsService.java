package com.appchat.activities.signupServices;


import androidx.annotation.NonNull;

public class ValidateInputsService {
    // TODO: make sure that the username is not already taken
    public static SignupErrors validateUsername(@NonNull String username) {
        if (username.isEmpty()) {
            return SignupErrors.EMPTY_FIELD;
        }
        String regex = "^[a-zA-Z0-9_#@-]{6,}$";
        boolean isValidInput = username.matches(regex);
        if (!isValidInput) {
            return SignupErrors.USERNAME_NOT_VALID;
        }
        return SignupErrors.OK;
    }

    public static SignupErrors validatePassword(@NonNull String input) {

        if (input.isEmpty()) {
            return SignupErrors.EMPTY_FIELD;
        }

        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[%&*!])[A-Za-z\\d%&*!]{8,}$";
        boolean isValidInput = input.matches(regex);


        if (!isValidInput) {
            return SignupErrors.PASSWORD_NOT_VALID;
        }

        return SignupErrors.OK;
    }

    public static SignupErrors validateConfirmPassword(String password, @NonNull String confirmPassword) {
        if (confirmPassword.isEmpty()) {
            return SignupErrors.EMPTY_FIELD;
        }
        if (!confirmPassword.equals(password)) {
            return SignupErrors.PASSWORDS_NOT_MATCH;
        }
        return SignupErrors.OK;
    }

    public static SignupErrors validatePasswords(String password, String confirmPassword) {
        SignupErrors passwordError = validatePassword(password);
        if (passwordError != SignupErrors.OK) {
            return passwordError;
        }
        return validateConfirmPassword(password, confirmPassword);
    }

    public  static SignupErrors validateDisplayName(@NonNull String displayName) {
        if (displayName.isEmpty()) {
            return SignupErrors.EMPTY_FIELD;
        }

        if (displayName.length() < 4) {
            return SignupErrors.DISPLAY_NAME_NOT_VALID;
        }

        // TODO: change later to check with simple regex
//        String regex = "^(?=[a-zA-Z0-9_\\-#@]{4,}$)(?=.*[a-zA-Z]{3,})(?=.*\\d)";

//        boolean isValidInput = displayName.matches(regex);
//        if (!isValidInput) {
//            return SignupErrors.DISPLAY_NAME_NOT_VALID;
//        }
        return SignupErrors.OK;
    }
}
