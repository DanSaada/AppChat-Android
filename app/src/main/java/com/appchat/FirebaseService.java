package com.appchat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.appchat.db.dao.UserDao;
import com.appchat.entities.Message;
import com.appchat.entities.User;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseService extends FirebaseMessagingService {
    public FirebaseService() {
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        MutableLiveData<String> contacts = SingletonFirebase.getFirebaseContactInstance();
        MutableLiveData<Message> messages = SingletonFirebase.getFirebaseMessageInstance();
        if (remoteMessage.getNotification() != null) {
            createNotificationChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                    .setSmallIcon(R.drawable.webchat_logo)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
            notificationManager.notify(1, builder.build());
        }
        Map<String, String> data = remoteMessage.getData();
        if (data.isEmpty()) {
            return;
        }
        if (data.get("action").equals("addNewContact")) {
            contacts.postValue("new contact");
        } else if (data.get("action").equals("sendNewMessage")) {
            String senderUsername = data.get("senderUsername");
            String receiver = data.get("receiver");
            String date = data.get("date");
            messages.postValue(new Message(remoteMessage.getNotification().getBody(), date, true, senderUsername, receiver));
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", "channel", importance);
            channel.setDescription("channel");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}