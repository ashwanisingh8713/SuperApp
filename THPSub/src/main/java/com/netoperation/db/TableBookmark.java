package com.netoperation.db;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.netoperation.model.ArticleBean;

@Entity(tableName = "TableBookmark")
public class TableBookmark {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    private String aid ;

    private ArticleBean bean;

    private String groupType;

    public TableBookmark(String aid, ArticleBean bean, String groupType) {
        this.aid = aid;
        this.bean = bean;
        this.groupType = groupType;
    }
    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
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
