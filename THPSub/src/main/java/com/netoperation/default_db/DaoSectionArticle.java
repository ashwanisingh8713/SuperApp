package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface DaoSectionArticle {


    @Insert
    void insertSectionArticle(TableSectionArticle tableSectionArticle);

    @Query("SELECT * FROM TableSectionArticle WHERE secId = :secId")
    List<TableSectionArticle> getAllArticles(String secId);

    @Query("SELECT * FROM TableSectionArticle WHERE secId = :secId AND page = :page")
    List<TableSectionArticle> getPageArticles(String secId, int page);


    @Query("DELETE FROM TableSectionArticle WHERE secId = :secId")
    int deleteSection(String secId);

    @Query("DELETE FROM TableSectionArticle")
    void deleteAll();

}
