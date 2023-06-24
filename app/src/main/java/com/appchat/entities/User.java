package com.appchat.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey()
    @NonNull
    private String username;
    private String password;
    private String displayName;
//    @TypeConverters(Base64TypeConverter.class)
//    // BOLB - Binary Large Object
//    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private String profilePic;

    @Ignore
    public User() {
        this.username = null;
        this.password = null;
        this.displayName = null;
        this.profilePic = null;
    }

    @Ignore
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.displayName = null;
        this.profilePic = null;
    }

    public User(String username, String password, String displayName, String profilePic) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.profilePic = profilePic;
    }

    //SETTERS
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    //GETTERS
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfilePic() {
        return profilePic;
    }
}
