package com.netoperation.default_db;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "TableMPReadArticle")
public class TableMPReadArticle {
    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String articleId;

    private boolean isArticleRestricted;

    private boolean isUserCanReRead;

    private int totalReadCount;

    private boolean isBannerCloseClick;

    @Ignore
    public TableMPReadArticle() {

    }


    public TableMPReadArticle(String articleId, boolean isArticleRestricted, boolean isUserCanReRead) {
        this.articleId = articleId;
        this.isArticleRestricted = isArticleRestricted;
        this.isUserCanReRead = isUserCanReRead;
    }

    public int getTotalReadCount() {
        return totalReadCount;
    }

    public void setTotalReadCount(int totalReadCount) {
        this.totalReadCount = totalReadCount;
    }

    public boolean isArticleRestricted() {
        return isArticleRestricted;
    }

    public void setArticleRestricted(boolean articleRestricted) {
        isArticleRestricted = articleRestricted;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public boolean isUserCanReRead() {
        return isUserCanReRead;
    }

    public void setUserCanReRead(boolean userCanReRead) {
        isUserCanReRead = userCanReRead;
    }

    public boolean isBannerCloseClick() {
        return isBannerCloseClick;
    }

    public void setBannerCloseClick(boolean bannerCloseClick) {
        isBannerCloseClick = bannerCloseClick;
    }
}
