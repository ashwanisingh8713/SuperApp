package com.netoperation.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.netoperation.model.UserProfile;

@Dao
public interface UserProfileDao {

    @Insert
    void insertUserProfile(UserProfileTable profileTable);

    @Query("SELECT * FROM UserProfileTable")
    UserProfileTable getUserProfileTable();

    @Query("UPDATE UserProfileTable SET userProfile = :userProfile WHERE userId = :userId")
    int updateUserProfile(String userId, UserProfile userProfile);

    @Query("DELETE FROM UserProfileTable")
    void deleteAll();
}
