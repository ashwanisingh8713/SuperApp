package com.netoperation.default_db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.netoperation.model.ArticleBean;

@Entity(tableName ="TableTemperoryArticle")
public class TableTemperoryArticle {

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


    public TableTemperoryArticle(String aid, ArticleBean bean) {
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
