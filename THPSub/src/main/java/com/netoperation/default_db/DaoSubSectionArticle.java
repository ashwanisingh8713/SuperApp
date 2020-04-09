package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoSubSectionArticle {

    @Insert
    void insertWidget(TableSubSectionArticle tableSubSectionArticle);

    @Query("SELECT * FROM TableSubSectionArticle WHERE secId = :secId")
    List<TableSubSectionArticle> getAllArticles(String secId);

    @Query("SELECT * FROM TableSubSectionArticle WHERE secId = :secId AND page = :page")
    List<TableSubSectionArticle> getPageArticles(String secId, int page);


    @Query("DELETE FROM TableSubSectionArticle WHERE secId = :secId")
    int deleteSection(String secId);

    @Query("DELETE FROM TableSubSectionArticle")
    void deleteAll();

}
