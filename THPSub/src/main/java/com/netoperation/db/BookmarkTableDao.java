package com.netoperation.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.netoperation.model.RecoBean;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public interface BookmarkTableDao {

    @Insert(onConflict = REPLACE)
    void insertBookmark(BookmarkTable bookmarkTable);

    @Query("SELECT * FROM BookmarkTable")
    List<BookmarkTable> getAllBookmark();

    @Query("SELECT * FROM BookmarkTable WHERE aid = :aid")
    BookmarkTable getBookmarkArticle(String aid);

    @Query("SELECT * FROM BookmarkTable WHERE aid = :aid")
    List<BookmarkTable> getBookmarkArticles(String aid);

    @Query("DELETE FROM BookmarkTable WHERE aid = :aid")
    int deleteBookmarkArticle(String aid);

    @Query("UPDATE BookmarkTable SET bean = :bean WHERE aid = :aid")
    int updateBookmark(String aid, RecoBean bean);

    @Query("DELETE FROM BookmarkTable")
    void deleteAll();
}
