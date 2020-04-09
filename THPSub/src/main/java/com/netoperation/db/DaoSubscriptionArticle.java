package com.netoperation.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.netoperation.model.ArticleBean;

import java.util.List;


@Dao
public interface DaoSubscriptionArticle {

//    @Insert(onConflict = ABORT)
    @Insert
    void insertDashboard(TableSubscriptionArticle HomeTable);

    @Query("SELECT * FROM TableSubscriptionArticle WHERE recoFrom = :recoFrom")
    List<TableSubscriptionArticle> getAllDashboardBean(String recoFrom);

    @Query("SELECT * FROM TableSubscriptionArticle WHERE aid = :aid")
    TableSubscriptionArticle getSingleDashboardBean(String aid);

    @Query("DELETE FROM TableSubscriptionArticle")
    void deleteAll();

    @Query("DELETE FROM TableSubscriptionArticle WHERE recoFrom = :recoFrom")
    void deleteAll(String recoFrom);

    @Query("UPDATE TableSubscriptionArticle SET bean = :bean WHERE aid = :aid")
    int updateRecobean(String aid, ArticleBean bean);


}
