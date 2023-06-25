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
//    public static NotificationManagerCompat notificationManagerCompat;

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
//        notificationManagerCompat = NotificationManagerCompat.from(this);
//        createNotificationChannel();
    }

//    public void createNotificationChannel() {
//        // check the version of the OS
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            // channel for contacts
//            CharSequence name = "Contact's channel";
//            String description = "channel with the server for receiving new contacts";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("1", name, importance);
//            channel.setDescription(description);
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//
//            // channel for messages
//            name = "Message's channel";
//            description = "channel with the server for receiving new messages";
//            importance = NotificationManager.IMPORTANCE_DEFAULT;
//            channel = new NotificationChannel("2", name, importance);
//            channel.setDescription(description);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
}
