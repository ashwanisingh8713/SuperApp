package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoHomeArticle {

    @Insert
    void insertWidget(TableHomeArticle tableHomeArticle);

    @Query("SELECT * FROM TableHomeArticle")
    List<TableHomeArticle> getArticles();

    @Query("DELETE FROM TableHomeArticle")
    void deleteAll();

}
