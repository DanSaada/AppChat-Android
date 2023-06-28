package com.appchat.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.appchat.entities.Message;

import java.util.List;

@Dao
public interface MessageDao {

    @Query("SELECT * FROM message")
    List<Message> index();

    @Query("SELECT * FROM message WHERE messageID = :id")
    Message get(int id);

    // TODO: check if this is correct, if not rollback to WHERE (sender = :contactId AND receiver = :userId) OR (sender = :userId AND receiver = :contactId)
    @Query("SELECT * FROM message WHERE message.chatID = :chatID")
    List<Message> getChatMessages(String chatID);

    @Insert
    void insert(List<Message> messages);

    @Update
    void update(Message... Messages);

    @Query("DELETE FROM message")
    void clear();
}
