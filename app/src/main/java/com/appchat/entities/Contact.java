package com.appchat.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;
    private String name;
    private String lastMsg;
    private String sentTime;
    private String userId; // the contact's real id in a prespective of a user in the db

    private int unreadCount;

    private String contactImage;

    public Contact() {
        this.id = null;
        this.name = null;
        this.lastMsg = null;
        this.sentTime = null;
        this.userId = null;
        this.unreadCount = 0;
        this.contactImage = null;
    }

    public Contact(String id, String name, String lastMsg, String sentTime, String userId, int unreadCount, String contactImage) {
        this.id = id;
        this.name = name;
        this.lastMsg = lastMsg;
        this.sentTime = sentTime;
        this.userId = userId;
        this.unreadCount = unreadCount;
        this.contactImage = contactImage;
    }


    //SETTERS
    public void setId(String contactId) {
        this.id = contactId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    //GETTERS
    public String getId() {
        return this.id;
    }

    public String getName() { return this.name;}

    public String getLastMsg() {
        return this.lastMsg;
    }

    public String getSentTime() {
        return this.sentTime;
    }

    public String getUserId() {
        return this.userId;
    }

    public int getUnreadCount() {
        return this.unreadCount;
    }

    public String getContactImage() {
        return this.contactImage;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public void setContactImage(String contactImage) {
        this.contactImage = contactImage;
    }
}
