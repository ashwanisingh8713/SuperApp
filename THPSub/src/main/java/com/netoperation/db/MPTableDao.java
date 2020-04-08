package com.netoperation.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Set;

import io.reactivex.Flowable;

@Dao
public interface MPTableDao {

    @Insert
    void insertMpTableData(MPTable mpTable);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMPTable(MPTable mpTable);

    @Query("SELECT * from MPTable")
    MPTable getMPTable();

    @Query("SELECT readArticleIds from MPTable")
    Flowable<Set> getArticleIdsFlowable();

    @Query("SELECT mpBannerMsg from MPTable")
    String getMpBannerMsg();

    @Query("SELECT allowedArticleCounts from MPTable")
    int getAllowedArticleCounts();

    @Query("SELECT allowedTimeInSecs from MPTable")
    int getAllowedArticleTimesInSecs();

    @Query("SELECT cycleName from MPTable")
    String getCycleName();

    @Query("SELECT isMpFeatureEnabled from MPTable")
    boolean isMpFeatureEnabled();

    @Query("SELECT readArticleIds from MPTable")
    Set getArticleIds();

    @Query("DELETE FROM MPTable")
    void deleteAll();

    @Query("SELECT startTimeInMillis from MPTable")
    long getStartTimeInMillis();

    @Query("SELECT expiryTimeInMillis from MPTable")
    long getExpiryTimeInMillis();

//    @Query("UPDATE MPTable SET readArticleIds = :newReadArticleIds WHERE id = :id")
//    int updateArticleIds(Set<String> newReadArticleIds, int id);

}
