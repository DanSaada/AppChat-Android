package com.appchat.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.appchat.models.converters.Base64TypeConverter;

@Entity
public class User {
    @PrimaryKey()
    @NonNull
    private String id;
    private String password;
    private String displayName;
    @TypeConverters(Base64TypeConverter.class)
    // BOLB - Binary Large Object
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] profilePic;

    @Ignore
    public User() {
        this.id = null;
        this.password = null;
        this.displayName = null;
        this.profilePic = null;
    }

    @Ignore
    public User(String id, String password) {
        this.id = id;
        this.password = password;
        this.displayName = null;
        this.profilePic = null;
    }

    public User(String id, String password, String Displayname, byte[] profilePic) {
        this.id = id;
        this.password = password;
        this.displayName = Displayname;
        this.profilePic = profilePic;
    }

    //SETTERS
    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }

    //GETTERS
    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public byte[] getProfilePic() {
        return profilePic;
    }
}
