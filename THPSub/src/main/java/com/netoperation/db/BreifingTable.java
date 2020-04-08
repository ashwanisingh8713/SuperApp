package com.netoperation.db;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.netoperation.model.RecoBean;

import java.util.List;

@Entity(tableName = "BreifingTable")
public class BreifingTable {

    @PrimaryKey(autoGenerate = true)
    private int id;


    private List<RecoBean> morning;
    private List<RecoBean> noon;
    private List<RecoBean> evening;

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

    public List<RecoBean> getMorning() {
        return morning;
    }

    public void setMorning(List<RecoBean> morning) {
        this.morning = morning;
    }

    public List<RecoBean> getNoon() {
        return noon;
    }

    public void setNoon(List<RecoBean> noon) {
        this.noon = noon;
    }

    public List<RecoBean> getEvening() {
        return evening;
    }

    public void setEvening(List<RecoBean> evening) {
        this.evening = evening;
    }
}
