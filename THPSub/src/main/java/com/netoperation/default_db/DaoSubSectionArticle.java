package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface DaoSubSectionArticle {

    @Insert
    void insertSubSectionArticle(TableSubSectionArticle tableSubSectionArticle);

    @Query("SELECT * FROM TableSubSectionArticle WHERE secId = :secId AND page = :page")
    Maybe<List<TableSubSectionArticle>> getPageArticlesMaybe(String secId, int page);

    @Query("SELECT * FROM TableSubSectionArticle WHERE secId = :secId")
    Maybe<List<TableSubSectionArticle>> getArticlesMaybe(String secId);

    @Query("SELECT * FROM TableSubSectionArticle")
    List<TableSubSectionArticle> getAllArticles();


    @Query("DELETE FROM TableSubSectionArticle WHERE secId = :secId")
    int deleteSection(String secId);

    @Query("DELETE FROM TableSubSectionArticle")
    void deleteAll();

}
