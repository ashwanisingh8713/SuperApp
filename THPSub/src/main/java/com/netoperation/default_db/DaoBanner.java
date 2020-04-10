package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.netoperation.model.ArticleBean;

import java.util.List;

@Dao
public interface DaoBanner {

    @Insert
    void insertWidget(TableBanner tableBanner);

    @Query("SELECT * FROM TableBanner")
    TableBanner getBanners();

    @Query("UPDATE TableBanner SET beans = :beans WHERE secId = :secId")
    int updateBannerArticles(String secId, List<ArticleBean> beans);

    @Query("DELETE FROM TableBanner WHERE secId = :secId")
    int deleteBanner(String secId);

    @Query("DELETE FROM TableBanner")
    void deleteAll();
}
