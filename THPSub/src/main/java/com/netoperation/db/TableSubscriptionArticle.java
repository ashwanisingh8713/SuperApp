package com.netoperation.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.netoperation.model.ArticleBean;

@Entity(tableName ="TableSubscriptionArticle")
public class TableSubscriptionArticle {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @ColumnInfo(name ="aid")
    private String aid ;

    @ColumnInfo(name ="bean")
    private ArticleBean bean;

    @ColumnInfo(name ="recoFrom")
    private String recoFrom;


    public TableSubscriptionArticle(String aid, String recoFrom, ArticleBean bean) {
        this.aid = aid;
        this.bean = bean;
        this.recoFrom = recoFrom;
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

    public String getRecoFrom() {
        return recoFrom;
    }

    public void setRecoFrom(String recoFrom) {
        this.recoFrom = recoFrom;
    }


}
