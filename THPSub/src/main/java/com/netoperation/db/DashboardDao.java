package com.netoperation.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.netoperation.model.ArticleBean;

import java.util.List;


@Dao
public interface DashboardDao {

//    @Insert(onConflict = ABORT)
    @Insert
    void insertDashboard(SubscriptionArticleTable HomeTable);

    @Query("SELECT * FROM SubscriptionArticleTable WHERE recoFrom = :recoFrom")
    List<SubscriptionArticleTable> getAllDashboardBean(String recoFrom);

    @Query("SELECT * FROM SubscriptionArticleTable WHERE aid = :aid")
    SubscriptionArticleTable getSingleDashboardBean(String aid);

    @Query("DELETE FROM SubscriptionArticleTable")
    void deleteAll();

    @Query("DELETE FROM SubscriptionArticleTable WHERE recoFrom = :recoFrom")
    void deleteAll(String recoFrom);

    @Query("UPDATE SubscriptionArticleTable SET bean = :bean WHERE aid = :aid")
    int updateRecobean(String aid, ArticleBean bean);


}
