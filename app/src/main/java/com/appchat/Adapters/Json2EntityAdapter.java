package com.appchat.Adapters;

import com.appchat.entities.Contact;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Json2EntityAdapter {

    public static Contact JsonToContact(JsonObject jsonContact) {
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

    public static List<Contact> Json2ContactList(List<JsonObject> jsonContacts) {
        List<Contact> contacts = new ArrayList<>();
        for (JsonObject contact : jsonContacts) {
            Contact contactObj = JsonToContact(contact);
            contacts.add(contactObj);
        }
        return contacts;
    }


}
