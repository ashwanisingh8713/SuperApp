package com.netoperation.default_db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.netoperation.model.ArticleBean;
import com.netoperation.model.StaticPageUrlBean;

@Entity(tableName = "TableHomeArticle")
public class TableHomeArticle {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    private StaticPageUrlBean staticPageBean;
    private String articleId ;

    private ArticleBean bean;


    public TableHomeArticle(String articleId, ArticleBean bean) {
        this.articleId = articleId;
        this.bean = bean;
    }

    public StaticPageUrlBean getStaticPageBean() {
        return staticPageBean;
    }

    public void setStaticPageBean(StaticPageUrlBean staticPageBean) {
        this.staticPageBean = staticPageBean;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public ArticleBean getBean() {
        return bean;
    }

    public void setBean(ArticleBean bean) {
        this.bean = bean;
    }
}
