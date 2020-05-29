package com.netoperation.default_db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.netoperation.model.ArticleBean;

import java.util.List;

@Entity(tableName = "TableRelatedArticle")
public class TableRelatedArticle {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    private String articleId;

    private List<ArticleBean> beans;


    public TableRelatedArticle(String articleId, List<ArticleBean> beans) {
        this.articleId = articleId;
        this.beans = beans;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public List<ArticleBean> getBeans() {
        return beans;
    }

    public void setBeans(List<ArticleBean> beans) {
        this.beans = beans;
    }


}
