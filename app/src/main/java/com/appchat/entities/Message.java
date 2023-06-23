package com.appchat.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.appchat.AppStateManager;

@Entity
public class Message {

    @PrimaryKey(autoGenerate = true)
    private int msgId;
    private String content;
    private String created;
    private boolean sent;
    private String userId;
    private String contactId;

    public Message() {

    }

    @Ignore
    public Message(String content, String created, boolean sent, String userId, String contactId) {
        // the id is auto generated
        this.content = content;
        this.created = created;
        this.sent = sent;
        this.userId = userId;
        this.contactId = contactId;
    }

    public boolean isSent() {
        return sent;
    }

    //SETTERS
    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    //GETTERS
    public int getMsgId() {
        return msgId;
    }

    public String getContent() {
        return content;
    }

    public String getCreated() {
        return created;
    }

    public String getUserId() {
        return userId;
    }

    public String getContactId() {
        return contactId;
    }

    public boolean isSentByLoggedUser() {
        return userId.equals(AppStateManager.loggedUser);
    }

    public boolean isReceivedByLoggedUser() {
        return contactId.equals(AppStateManager.loggedUser);
    }
}
