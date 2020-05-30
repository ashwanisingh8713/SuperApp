package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface DaoRelatedArticle {

    @Insert
    void insertSectionArticle(TableRelatedArticle tableRelatedArticle);

    @Query("SELECT * FROM TableRelatedArticle WHERE articleId = :articleId")
    Maybe<TableRelatedArticle> getAllArticles(String articleId);

    @Query("SELECT * FROM TableRelatedArticle")
    List<TableRelatedArticle> getAllArticles();

    @Query("SELECT * FROM TableRelatedArticle WHERE articleId = :articleId")
    Maybe<List<TableRelatedArticle>> getArticlesMaybe(String articleId);

    @Query("DELETE FROM TableRelatedArticle WHERE articleId = :articleId")
    int deleteRelatedAllArticle(String articleId);

    @Query("DELETE FROM TableRelatedArticle")
    void deleteAll();

}
