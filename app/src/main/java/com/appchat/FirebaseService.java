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

//    @Override
//    public void onMessageReceived1(@NonNull RemoteMessage remoteMessage) {
//        String type = "type";
//        String user = "User";
//
//        // check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            String typeGot = remoteMessage.getData().get(type);
//            String userGot = remoteMessage.getData().get(user);
//
//            // if got a new contact message, then show a proper notification
//            if (typeGot.compareTo("Contact") == 0) {
//                if (remoteMessage.getNotification() != null &&
//                        (AppStateManager.loggedUser == null || AppStateManager.loggedUser.length() == 0 ||
//                                userGot.compareTo(AppStateManager.loggedUser) == 0)) {
//                    NotificationCompat.Builder builder = new NotificationCompat
//                            .Builder(this, "1")
//                            .setSmallIcon(R.drawable.webchat_logo)
//                            .setContentTitle(remoteMessage.getNotification().getTitle())
//                            .setContentText(remoteMessage.getNotification().getBody())
//                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//                    // show a notification to the user
//                    AppStateManager.notificationManagerCompat.notify(contactsCounter++, builder.build());
//                    // inform other components within the app about the new contact notification.
//                    Intent intent = new Intent("notifyContact");
//                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//                }
//            } else if (typeGot.compareTo("Message") == 0) {
//
//                // if got a new message message, then show a proper notification
//                if (remoteMessage.getNotification() != null &&
//                        (AppStateManager.loggedUser == null || AppStateManager.loggedUser.length() == 0 ||
//                                userGot.compareTo(AppStateManager.loggedUser) == 0)) {
//                    NotificationCompat.Builder builder = new NotificationCompat
//                            .Builder(this, "2")
//                            .setSmallIcon(R.drawable.webchat_logo)
//                            .setContentTitle("New message")
//                            .setContentText(remoteMessage.getNotification().getTitle())
//                            .setStyle(new NotificationCompat.BigTextStyle().bigText
//                                    (remoteMessage.getNotification().getBody()))
//                            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//                    AppStateManager.notificationManagerCompat.notify(messagesCounter++, builder.build());
//                    Intent intent = new Intent("notifyMessage");
//                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//                }
//            }
//        }
//    }


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
            // TODO: check for bugs from this if statement
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
            contacts.postValue("contact added");
        } else if (data.get("action").equals("sendNewMessage")) {
            String chatID = data.get("chatID");
            String senderUsername = data.get("senderUsername");
            String senderDisplayName = data.get("senderDisplayName");
            String receiver = data.get("receiver");
            String msgID = data.get("msgID");
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