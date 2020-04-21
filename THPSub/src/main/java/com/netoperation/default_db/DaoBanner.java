package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.netoperation.model.ArticleBean;

import java.lang.reflect.Method;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public abstract class DaoBanner {

    @Insert
    public abstract void insertBanner(TableBanner tableBanner);

    @Query("SELECT * FROM TableBanner")
    public abstract Observable<TableBanner> getBannersObservable();

    @Query("SELECT * FROM TableBanner")
    public abstract Single<TableBanner> getBannersSingle();

    @Query("SELECT * FROM TableBanner")
    public abstract TableBanner getBanners();

    @Query("UPDATE TableBanner SET beans = :beans WHERE secId = :secId")
    public abstract int updateBannerArticles(String secId, List<ArticleBean> beans);

    @Query("DELETE FROM TableBanner WHERE secId = :secId")
    public abstract int deleteBanner(String secId);

    @Query("DELETE FROM TableBanner")
    public abstract void deleteAll();
}
