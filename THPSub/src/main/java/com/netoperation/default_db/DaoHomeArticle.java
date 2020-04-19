package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.netoperation.model.ArticleBean;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface DaoHomeArticle {

    @Insert
    void insertHomeArticle(TableHomeArticle tableHomeArticle);

    @Query("SELECT * FROM TableHomeArticle")
    Observable<List<TableHomeArticle>> getArticles();

    @Query("SELECT * FROM TableHomeArticle")
    Single<List<TableHomeArticle>> getArticlesSingle();

    @Query("DELETE FROM TableHomeArticle WHERE secId = :secId")
    void delete(String secId);

    @Query("DELETE FROM TableHomeArticle")
    void deleteAll();

}
