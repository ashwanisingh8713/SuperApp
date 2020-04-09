package com.netoperation.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Set;

import io.reactivex.Flowable;

@Dao
public interface DaoMP {

    @Insert
    void insertMpTableData(TableMP tableMP);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMPTable(TableMP tableMP);

    @Query("SELECT * from TableMP")
    TableMP getMPTable();

    @Query("SELECT readArticleIds from TableMP")
    Flowable<Set> getArticleIdsFlowable();

    @Query("SELECT mpBannerMsg from TableMP")
    String getMpBannerMsg();

    @Query("SELECT allowedArticleCounts from TableMP")
    int getAllowedArticleCounts();

    @Query("SELECT allowedTimeInSecs from TableMP")
    int getAllowedArticleTimesInSecs();

    @Query("SELECT cycleName from TableMP")
    String getCycleName();

    @Query("SELECT isMpFeatureEnabled from TableMP")
    boolean isMpFeatureEnabled();

    @Query("SELECT readArticleIds from TableMP")
    Set getArticleIds();

    @Query("DELETE FROM TableMP")
    void deleteAll();

    @Query("SELECT startTimeInMillis from TableMP")
    long getStartTimeInMillis();

    @Query("SELECT expiryTimeInMillis from TableMP")
    long getExpiryTimeInMillis();

//    @Query("UPDATE TableMP SET readArticleIds = :newReadArticleIds WHERE id = :id")
//    int updateArticleIds(Set<String> newReadArticleIds, int id);

}
