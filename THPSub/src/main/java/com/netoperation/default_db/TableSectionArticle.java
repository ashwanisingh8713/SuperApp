package com.netoperation.default_db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.netoperation.model.ArticleBean;

import java.util.List;

@Entity(tableName = "TableSectionArticle")
public class TableSectionArticle {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    private String secId ;
    private String secName ;

    private List<ArticleBean> beans;

    private int page;


    public TableSectionArticle(String secId, String secName, int page, List<ArticleBean> beans) {
        this.secId = secId;
        this.secName = secName;
        this.page = page;
        this.beans = beans;
    }

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }

    public List<ArticleBean> getBeans() {
        return beans;
    }

    public void setBeans(List<ArticleBean> beans) {
        this.beans = beans;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getSecName() {
        return secName;
    }

    public void setSecName(String secName) {
        this.secName = secName;
    }
}
