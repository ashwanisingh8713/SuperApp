package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
import java.util.Set;

import io.reactivex.Flowable;
import io.reactivex.Single;


@Dao
public abstract class  DaoRead {

    @Insert
    public abstract void insertReadArticle(TableRead tableRead);

    @Query("SELECT * FROM TableRead")
    public abstract Single<List<TableRead>> getAllReadArticleId();

    @Query("SELECT COUNT(*) FROM TableRead WHERE groupType = :groupT")
    public abstract int getAllReadArticlesCount(String groupT);

    @Query("SELECT * FROM TableRead WHERE articleId = :articleId")
    public abstract TableRead getReadArticleId(String articleId);

    @Query("UPDATE TableRead SET commentCount = :commentCount, lutOfCommentCount =:lutOfCommentCount WHERE articleId = :articleId")
    public abstract int updateReadTable(String articleId, String commentCount, long lutOfCommentCount);

    @Query("DELETE FROM TableRead WHERE articleId = :articleId")
    public abstract int deleteReadArticleId(String articleId);

    @Query("delete from TableRead where articleId in (:idList)")
    public abstract int deleteMultiArticleId(List<String> idList);

}
