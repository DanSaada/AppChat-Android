package com.appchat.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.appchat.db.dao.UserDao;
import com.appchat.entities.User;

@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    public abstract UserDao userDao();
}
