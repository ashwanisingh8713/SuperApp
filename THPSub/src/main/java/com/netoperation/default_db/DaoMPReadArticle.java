package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;


@Dao
public abstract class DaoMPReadArticle {

    @Insert
    public abstract void insertReadArticle(TableMPReadArticle tableRead);

    @Query("SELECT articleId FROM TableMPReadArticle WHERE articleId = :articleId")
    public abstract String getMPReadArticleId(String articleId);

    @Query("UPDATE TableMPReadArticle SET isArticleRestricted = :isArticleRestricted WHERE articleId = :articleId")
    public abstract int updateMpReadArticle(String articleId, boolean isArticleRestricted);

    @Query("UPDATE TableMPReadArticle SET isBannerCloseClick = :isBannerCloseClick WHERE articleId = :articleId")
    public abstract int updateIsBannerCloseForArticle(String articleId, boolean isBannerCloseClick);

    @Query("SELECT * FROM TableMPReadArticle WHERE articleId = :articleId")
    public abstract Maybe<TableMPReadArticle> isArticleRestricted(String articleId);

    @Query("SELECT articleId from TableMPReadArticle WHERE articleId = :articleId AND isArticleRestricted =1")
    public abstract Flowable<String> getRestrictedArticleId(String articleId);

    @Query("SELECT articleId from TableMPReadArticle WHERE isArticleRestricted =1")
    public abstract Flowable<List<String>> getAllRestrictedArticleIdsFLowable();

    @Query("SELECT articleId from TableMPReadArticle WHERE isArticleRestricted =1")
    public abstract List<String> getAllRestrictedArticleIds();


    @Query("DELETE FROM TableMPReadArticle")
    public abstract int DELETE();


}
