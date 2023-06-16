package com.appchat;

import android.app.Application;
import android.content.Context;
import androidx.room.Room;
import com.appchat.db.UsersDB;

public class AppStateManager extends Application {
    public static Context context;
    public static String loggedUser;
    public static String loggerUserToken;
    public static boolean isLogged;
    public static UsersDB usersDB;
    public static String serverUrl;
    public static String serverPort;

    @Override
    public void onCreate() {
        super.onCreate();
        serverUrl = this.getString(R.string.serverUrl);
        serverPort = this.getString(R.string.serverPort);
        context = getApplicationContext();
        loggedUser = null;
        loggerUserToken = null;
        isLogged = false;
        usersDB = Room.databaseBuilder(context, UsersDB.class, "user")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
    }
}
