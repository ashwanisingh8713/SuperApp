package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface DaoSectionArticle {


    @Insert
    void insertSectionArticle(TableSectionArticle tableSectionArticle);

    @Query("SELECT * FROM TableSectionArticle WHERE secId = :secId")
    List<TableSectionArticle> getAllArticles(String secId);

    @Query("SELECT * FROM TableSectionArticle")
    List<TableSectionArticle> getAllArticles();

    @Query("SELECT * FROM TableSectionArticle WHERE secId = :secId AND page = :page")
    Observable<TableSectionArticle> getPageArticlesObservable(String secId, int page);

    @Query("SELECT * FROM TableSectionArticle WHERE secId = :secId AND page = :page")
    Single<List<TableSectionArticle>> getPageArticlesMaybe(String secId, int page);

    @Query("SELECT * FROM TableSectionArticle WHERE secId = :secId")
    Maybe<List<TableSectionArticle>> getArticlesMaybe(String secId);

    @Query("SELECT * FROM TableSectionArticle WHERE secId = :secId AND page = :page")
    TableSectionArticle getPageArticles(String secId, int page);

    @Query("DELETE FROM TableSectionArticle WHERE secId = :secId")
    int deleteSection(String secId);

    @Query("DELETE FROM TableSectionArticle")
    void deleteAll();

}
