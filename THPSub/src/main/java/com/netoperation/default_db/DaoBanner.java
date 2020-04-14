package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.netoperation.model.ArticleBean;

import java.lang.reflect.Method;
import java.util.List;

import io.reactivex.Observable;

@Dao
public abstract class DaoBanner {

    @Insert
    public abstract void insertBanner(TableBanner tableBanner);

    @Query("SELECT * FROM TableBanner")
    public abstract Observable<TableBanner> getBannersObservable();

    @Query("SELECT * FROM TableBanner")
    public abstract TableBanner getBanners();

    @Query("UPDATE TableBanner SET beans = :beans WHERE secId = :secId")
    public abstract int updateBannerArticles(String secId, List<ArticleBean> beans);

    @Transaction
    public void deleteAndInsertInBanner(TableBanner tableBanner) {
        deleteAll();
        insertBanner(tableBanner);
    }


    @Query("DELETE FROM TableBanner WHERE secId = :secId")
    public abstract int deleteBanner(String secId);

    @Query("DELETE FROM TableBanner")
    public abstract void deleteAll();
}
