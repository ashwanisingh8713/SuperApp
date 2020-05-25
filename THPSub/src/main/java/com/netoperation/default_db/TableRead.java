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

    private String commentCount;

    private long lutOfCommentCount;
    private String groupType;

    public TableRead(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public long getLutOfCommentCount() {
        return lutOfCommentCount;
    }

    public void setLutOfCommentCount(long lutOfCommentCount) {
        this.lutOfCommentCount = lutOfCommentCount;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }
}
