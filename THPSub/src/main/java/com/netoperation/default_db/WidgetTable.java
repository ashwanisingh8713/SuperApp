package com.netoperation.default_db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "WidgetTable")
public class WidgetTable {

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
