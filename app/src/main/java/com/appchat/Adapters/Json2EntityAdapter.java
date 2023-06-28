package com.appchat.Adapters;

import androidx.annotation.NonNull;

import com.appchat.AppStateManager;
import com.appchat.entities.Contact;
import com.appchat.entities.Message;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Json2EntityAdapter {

    @NonNull
    public static Contact JsonToContact(@NonNull JsonObject jsonContact) {
        String id = jsonContact.getAsJsonPrimitive("id").getAsString();
        JsonObject userObject = jsonContact.getAsJsonObject("user");
        String displayName = userObject.getAsJsonPrimitive("displayName")
                .getAsString();
        String username = userObject.getAsJsonPrimitive("username")
                .getAsString();
        String profilePic = userObject.getAsJsonPrimitive("profilePic")
                .getAsString();
        JsonElement lastMessageElement = jsonContact.get("lastMessage");
        Contact contact;
        if (lastMessageElement == null || lastMessageElement.isJsonNull()) {
            contact = new Contact(id, displayName, null, null, username,
                    0, profilePic);
        } else {
            JsonObject lastMessageObject = lastMessageElement.getAsJsonObject();
            String created = lastMessageObject.getAsJsonPrimitive("created")
                    .getAsString();
            String content = lastMessageObject.getAsJsonPrimitive("content")
                    .getAsString();
            contact = new Contact(id, displayName, content, created, username,
                    0, profilePic);
        }
        return contact;
    }

    @NonNull
    public static List<Contact> Json2ContactList(@NonNull List<JsonObject> jsonContacts) {
        List<Contact> contacts = new ArrayList<>();
        for (JsonObject contact : jsonContacts) {
            Contact contactObj = JsonToContact(contact);
            contacts.add(contactObj);
        }
        return contacts;
    }

    @NonNull
    public static List<Message> Json2MessageList(@NonNull List<JsonObject> jsonMessages) {
        List<Message> messages = new ArrayList<>();
        for (JsonObject message : jsonMessages) {
            Message messageObj = JsonToMessage(message);
            messages.add(messageObj);
        }
        return messages;
    }

    @NonNull
    public static Message JsonToMessage(@NonNull JsonObject jsonMessage) {
        String content = jsonMessage.getAsJsonPrimitive("content").getAsString();
        //TODO: might be needed to parse the created date
        String created = timestampToDate(jsonMessage.
                getAsJsonPrimitive("created").getAsString());
        JsonObject senderObject = jsonMessage.getAsJsonObject("sender");
        String sender = senderObject.getAsJsonPrimitive("username").getAsString();
        Message message = new Message(content, created, true, sender,
                AppStateManager.contactId);
        return message;
    }

    @NonNull
    private static String timestampToDate(String timestamp) {
        DateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        inputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        DateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputDateFormat.setTimeZone(TimeZone.getDefault());

        String convertedTimestamp = null; // TODO: check that doesnt cause bugs
        try {
            Date date = inputDateFormat.parse(timestamp);
            convertedTimestamp = outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedTimestamp;
    }

}
