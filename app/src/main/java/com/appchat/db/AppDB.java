package com.appchat.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.appchat.db.dao.ContactDao;
import com.appchat.db.dao.MessageDao;
import com.appchat.entities.Contact;
import com.appchat.entities.Message;

@Database(entities = {Contact.class, Message.class}, version = 10, exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    public abstract ContactDao contactDao();
    public abstract MessageDao messageDao();
}
