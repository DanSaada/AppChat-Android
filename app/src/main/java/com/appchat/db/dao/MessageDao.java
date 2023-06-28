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

    @Query("SELECT * FROM message WHERE (sender = :userId AND receiver = :contactId) OR (sender = :contactId AND receiver = :userId)")
    List<Message> getChatMessages(String userId, String contactId);

    @Insert
    void insert(Message... Messages);

    @Update
    void update(Message... Messages);

    @Query("DELETE FROM message")
    void clear();
}
