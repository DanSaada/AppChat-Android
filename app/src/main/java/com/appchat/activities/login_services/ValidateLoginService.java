package com.appchat.activities.login_services;

public class ValidateLoginService {
    public static LoginErrors validate(String username, String password) {
        if (username.isEmpty() && password.isEmpty()) {
            return LoginErrors.BOTH_FIELDS_EMPTY;
        }
        if (username.isEmpty()) {
            return LoginErrors.EMPTY_FIELD_USERNAME;
        }
        if (password.isEmpty()) {
            return LoginErrors.EMPTY_FIELD_PASSWORD;
        }

        // TODO: check if the username exists in the database
        // if exists and validation was successfull return ok
        return LoginErrors.OK;
    }
}
