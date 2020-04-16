package com.netoperation.default_db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "TableRead")
public class TableRead {
    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String articleId;

    public TableRead(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
}
