package com.appchat;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;
import com.appchat.db.UsersDB;

public class AppStateManager extends Application {
    public static Context context;
    public static String loggedUser;
    public static String loggerUserToken;
    public static boolean isLogged;
    public static String contactId;
    public static UsersDB usersDB;
    public static String serverUrl;
    public static String serverPort;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        loggedUser = null;
        loggerUserToken = null;
        isLogged = false;
        contactId = null;
        usersDB = Room.databaseBuilder(context, UsersDB.class, "user")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        serverUrl = this.getString(R.string.serverUrl);
        serverPort = this.getString(R.string.serverPort);
    }
}
