package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.netoperation.model.ArticleBean;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface DaoHomeArticle {

    @Insert
    void insertWidget(TableHomeArticle tableHomeArticle);

    @Query("SELECT * FROM TableHomeArticle")
    Observable<List<TableHomeArticle>> getArticles();

    @Query("UPDATE TableHomeArticle SET beans = :beans WHERE secId = :secId")
    int updateHomeArticles(String secId, List<ArticleBean> beans);

    @Query("DELETE FROM TableHomeArticle")
    void deleteAll();

}
