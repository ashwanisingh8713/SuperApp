package com.netoperation.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.netoperation.model.UserProfile;

@Entity(tableName = "UserProfileTable")
public class UserProfileTable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name ="userId")
    private String userId;

    @ColumnInfo(name ="userProfile")
    private UserProfile userProfile;

    public UserProfileTable(String userId, UserProfile userProfile) {
        this.userId = userId;
        this.userProfile = userProfile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
