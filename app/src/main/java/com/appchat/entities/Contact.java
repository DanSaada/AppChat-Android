package com.appchat.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;
    private String displayName;
    private String content;
    private String created;
    private String username; // the contact's real id in a prespective of a user in the db

    private int unreadCount; // might be always 0 because we don't have a way to know if the user read the message or not and because its not in the server api

    private String profilePic;

    public Contact() {
        this.id = null;
        this.displayName = null;
        this.content = null;
        this.created = null;
        this.username = null;
        this.unreadCount = 0;
        this.profilePic = null;
    }

    public Contact(String id, String displayName, String content, String created, String username, int unreadCount, String profilePic) {
        this.id = id;
        this.displayName = displayName;
        this.content = content;
        this.created = created;
        this.username = username;
        this.unreadCount = unreadCount;
        this.profilePic = profilePic;
    }


    //SETTERS
    public void setId(String contactId) {
        this.id = contactId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //GETTERS
    public String getId() {
        return this.id;
    }

    public String getDisplayName() { return this.displayName;}

    public String getContent() {
        return this.content;
    }

    public String getCreated() {
        return this.created;
    }

    public String getUsername() {
        return this.username;
    }

    public int getUnreadCount() {
        return this.unreadCount;
    }

    public String getProfilePic() {
        return this.profilePic;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
