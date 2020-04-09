package com.netoperation.db;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.netoperation.model.ArticleBean;

import java.util.List;

@Entity(tableName = "TableBreifing")
public class TableBreifing {

    @PrimaryKey(autoGenerate = true)
    private int id;


    private List<ArticleBean> morning;
    private List<ArticleBean> noon;
    private List<ArticleBean> evening;

    private String morningTime;
    private String noonTime ;
    private String eveningTime;

    public String getMorningTime() {
        return morningTime;
    }

    public void setMorningTime(String morningTime) {
        this.morningTime = morningTime;
    }

    public String getNoonTime() {
        return noonTime;
    }

    public void setNoonTime(String noonTime) {
        this.noonTime = noonTime;
    }

    public String getEveningTime() {
        return eveningTime;
    }

    public void setEveningTime(String eveningTime) {
        this.eveningTime = eveningTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ArticleBean> getMorning() {
        return morning;
    }

    public void setMorning(List<ArticleBean> morning) {
        this.morning = morning;
    }

    public List<ArticleBean> getNoon() {
        return noon;
    }

    public void setNoon(List<ArticleBean> noon) {
        this.noon = noon;
    }

    public List<ArticleBean> getEvening() {
        return evening;
    }

    public void setEvening(List<ArticleBean> evening) {
        this.evening = evening;
    }
}
