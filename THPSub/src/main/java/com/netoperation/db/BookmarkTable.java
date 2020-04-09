package com.netoperation.db;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.netoperation.model.ArticleBean;

@Entity(tableName = "BookmarkTable")
public class BookmarkTable {


    @PrimaryKey
    @NonNull
    private String aid ;

    private ArticleBean bean;


    public BookmarkTable(String aid, ArticleBean bean) {
        this.aid = aid;
        this.bean = bean;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public ArticleBean getBean() {
        return bean;
    }

    public void setBean(ArticleBean bean) {
        this.bean = bean;
    }
}
