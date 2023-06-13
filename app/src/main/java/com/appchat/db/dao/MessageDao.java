package com.appchat.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.appchat.models.Message;
import java.util.List;

@Dao
public interface MessageDao {

    @Query("SELECT * FROM message")
    List<Message> index();

    @Query("SELECT * FROM message WHERE msgId = :id")
    Message get(int id);

    @Query("SELECT * FROM message WHERE userId = :userId AND contactId = :contactId")
    List<Message> getChatMessages(String userId, String contactId);

    @Insert
    void insert(Message... Messages);

    @Update
    void update(Message... Messages);

    @Delete
    void delete(Message... Messages);

    @Query("DELETE FROM message")
    void clear();
}
