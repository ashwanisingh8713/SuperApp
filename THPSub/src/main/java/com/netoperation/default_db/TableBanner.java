package com.netoperation.default_db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.netoperation.model.ArticleBean;
import com.netoperation.model.StaticPageUrlBean;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "TableBanner")
public class TableBanner {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String secId ;
    private String secName;
    private String type;
    private String lastUpdatedTime;

    private StaticPageUrlBean staticPageBean;

    private List<ArticleBean> beans = new ArrayList<>();

    public TableBanner(String secId, String secName, String type, String lastUpdatedTime, StaticPageUrlBean staticPageBean) {
        this.secId = secId;
        this.secName = secName;
        this.type = type;
        this.lastUpdatedTime = lastUpdatedTime;
        this.staticPageBean = staticPageBean;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(String lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public StaticPageUrlBean getStaticPageBean() {
        return staticPageBean;
    }

    public void setStaticPageBean(StaticPageUrlBean staticPageBean) {
        this.staticPageBean = staticPageBean;
    }

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }

    public String getSecName() {
        return secName;
    }

    public void setSecName(String secName) {
        this.secName = secName;
    }

    public List<ArticleBean> getBeans() {
        return beans;
    }

    public void setBeans(List<ArticleBean> beans) {
        this.beans = beans;
    }
}
