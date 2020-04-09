package com.netoperation.default_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.netoperation.model.ArticleBean;

import java.util.List;


@Dao
public interface DaoWidget {

    @Insert
    void insertWidget(TableWidget tableWidget);

    @Query("SELECT * FROM TableWidget")
    List<TableWidget> getWidgets();

    @Query("UPDATE TableWidget SET beans = :beans WHERE secId = :secId")
    int updateWidgetArticles(String secId, List<ArticleBean> beans);

    @Query("DELETE FROM TableWidget WHERE secId = :secId")
    int deleteWidget(String secId);

    @Query("DELETE FROM TableWidget")
    void deleteAll();

}
