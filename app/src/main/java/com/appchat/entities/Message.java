package com.appchat.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.appchat.AppStateManager;

@Entity
public class Message {

    @PrimaryKey(autoGenerate = true)
    private int messageID;
    private int chatID; // the id of the chat that this message belongs to FIXME: this is not used, might not be needed
    private String content;
    private String created;
    private boolean sent;
    private String sender; // previous version: private String contactId;
    private String receiver;


    public Message() {

    }

    @Ignore
    public Message(String content, String created, boolean sent, String sender, String receiver) {
        // the id is auto generated
        this.content = content;
        this.created = created;
        this.sent = sent;
        this.sender = sender;
        this.receiver = receiver;
    }

    @Ignore
    public Message(String content, String created, boolean sent, String sender, String receiver, int chatID) {
        // the id is auto generated
        this.content = content;
        this.created = created;
        this.sent = sent;
        this.sender = sender;
        this.receiver = receiver;
        this.chatID = chatID;
    }

    public boolean isSent() {
        return sent;
    }

    //SETTERS
    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    //GETTERS
    public int getMessageID() {
        return messageID;
    }

    public String getContent() {
        return content;
    }

    public String getCreated() {
        return created;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public int getChatID() {
        return chatID;
    }

    public void setChatID(int chatID) {
        this.chatID = chatID;
    }

    public boolean isSentByLoggedUser() {
        return sender.equals(AppStateManager.loggedUser);
    }

    public boolean isReceivedByLoggedUser() {
        return receiver.equals(AppStateManager.loggedUser);
    }
}
