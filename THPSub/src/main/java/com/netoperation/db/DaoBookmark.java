package com.netoperation.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.netoperation.model.ArticleBean;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public interface DaoBookmark {

    @Insert(onConflict = REPLACE)
    void insertBookmark(TableBookmark tableBookmark);

    @Query("SELECT * FROM TableBookmark")
    List<TableBookmark> getAllBookmark();

    @Query("SELECT * FROM TableBookmark WHERE aid = :aid")
    TableBookmark getBookmarkArticle(String aid);

    @Query("SELECT * FROM TableBookmark WHERE groupType = :groupType")
    List<TableBookmark> getBookmarkGroupType(String groupType);

    @Query("DELETE FROM TableBookmark WHERE aid = :aid")
    int deleteBookmarkArticle(String aid);

    @Query("UPDATE TableBookmark SET bean = :bean WHERE aid = :aid")
    int updateBookmark(String aid, ArticleBean bean);

    @Query("DELETE FROM TableBookmark WHERE groupType = :groupType")
    void delete(String groupType);

    @Query("DELETE FROM TableBookmark")
    void deleteAll();
}
