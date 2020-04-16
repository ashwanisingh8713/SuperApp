package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Single;


@Dao
public abstract class  DaoRead {

    @Insert
    public abstract void insertReadArticle(TableRead tableRead);

    @Query("SELECT * FROM TableRead WHERE articleId = :articleId")
    public abstract TableRead getReadArticleId(String articleId);

    @Query("SELECT * FROM TableRead")
    public abstract Single<List<TableRead>> getAllReadArticleId();

    @Query("DELETE FROM TableRead WHERE articleId = :articleId")
    public abstract int deleteReadArticleId(String articleId);

    @Query("delete from TableRead where articleId in (:idList)")
    public abstract int deleteMultiArticleId(List<String> idList);

}
