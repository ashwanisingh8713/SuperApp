package com.netoperation.default_db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.netoperation.model.ArticleBean;

import java.util.List;

@Entity(tableName = "TableWidget")
public class TableWidget {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int position;

    private int secId;
    private String secName;
    private String type;
    private boolean viewAllCTA;

    private List<ArticleBean> beans;

    public TableWidget(int position, int secId, String secName, String type, boolean viewAllCTA, List<ArticleBean> beans) {
        this.position = position;
        this.secId = secId;
        this.secName = secName;
        this.type = type;
        this.viewAllCTA = viewAllCTA;
        this.beans = beans;
    }

    public List<ArticleBean> getBeans() {
        return beans;
    }

    public void setBeans(List<ArticleBean> beans) {
        this.beans = beans;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getSecId() {
        return secId;
    }

    public void setSecId(int secId) {
        this.secId = secId;
    }

    public String getSecName() {
        return secName;
    }

    public void setSecName(String secName) {
        this.secName = secName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isViewAllCTA() {
        return viewAllCTA;
    }

    public void setViewAllCTA(boolean viewAllCTA) {
        this.viewAllCTA = viewAllCTA;
    }





}
