package com.netoperation.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.netoperation.model.UserProfile;

@Dao
public interface DaoUserProfile {

    @Insert
    void insertUserProfile(TableUserProfile profileTable);

    @Query("SELECT * FROM TableUserProfile")
    TableUserProfile getUserProfileTable();

    @Query("UPDATE TableUserProfile SET userProfile = :userProfile WHERE userId = :userId")
    int updateUserProfile(String userId, UserProfile userProfile);

    @Query("DELETE FROM TableUserProfile")
    void deleteAll();
}
